package com.importer;

import com.dao.ActorDAO;
import com.dao.GenreDAO;
import com.dao.MovieDAO;
import com.model.Actor;
import com.model.Genre;
import com.model.Movie;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Imports data from "The Movies Dataset" (available on Kaggle).
 *
 * Files needed (place in project root, or pass full paths):
 *   movies_metadata.csv  — columns used: id, title, release_date, runtime,
 *                          vote_average, genres
 *   credits.csv          — columns used: id (matches movies_metadata.id), cast
 *
 * The full dataset has 45 000+ rows. Use the maxRows parameter to limit
 * how many movies are imported during testing.
 */
public class MovieLensImporter {

    private final GenreDAO genreDAO;
    private final ActorDAO actorDAO;
    private final MovieDAO movieDAO;

    // In-memory caches to avoid redundant DB round-trips
    private final Map<String, Genre> genreCache = new HashMap<>();
    private final Map<String, Actor> actorCache  = new HashMap<>();

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MovieLensImporter(GenreDAO genreDAO, ActorDAO actorDAO, MovieDAO movieDAO) {
        this.genreDAO = genreDAO;
        this.actorDAO = actorDAO;
        this.movieDAO = movieDAO;
    }

    /**
     * Imports up to {@code maxRows} movies, then links their actors.
     *
     * @param moviesCsvPath  path to movies_metadata.csv
     * @param creditsCsvPath path to credits.csv
     * @param maxRows        maximum number of movies to import
     */
    public void importData(String moviesCsvPath, String creditsCsvPath, int maxRows)
            throws IOException, CsvValidationException, SQLException {

        System.out.println("[Importer] Reading movies from: " + moviesCsvPath);
        Map<String, Integer> idMap = importMovies(moviesCsvPath, maxRows);

        System.out.println("[Importer] Reading credits from: " + creditsCsvPath);
        importCredits(creditsCsvPath, idMap);

        System.out.println("[Importer] Finished. " + idMap.size() + " movies imported.");
    }

    // ------------------------------------------------------------------ //
    //  Private helpers                                                      //
    // ------------------------------------------------------------------ //

    /** Reads movies_metadata.csv and inserts each row. Returns a map of datasetId → dbId. */
    private Map<String, Integer> importMovies(String path, int maxRows)
            throws IOException, CsvValidationException, SQLException {

        Map<String, Integer> idMap = new HashMap<>();
        int count = 0;

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            Map<String, Integer> col = indexHeaders(reader.readNext());
            String[] row;

            while ((row = reader.readNext()) != null && count < maxRows) {
                try {
                    String datasetId = safeGet(row, col, "id");
                    String title     = safeGet(row, col, "title");
                    if (title.isBlank() || datasetId.isBlank()) continue;

                    LocalDate releaseDate = parseDate(safeGet(row, col, "release_date"));
                    int       duration    = parseInt(safeGet(row, col, "runtime"));
                    double    score       = parseDouble(safeGet(row, col, "vote_average"));
                    Genre     genre       = parseFirstGenre(safeGet(row, col, "genres"));

                    Movie movie = movieDAO.create(title, releaseDate, duration, score,
                            genre != null ? genre.getId() : 0);

                    idMap.put(datasetId, movie.getId());
                    count++;

                    if (count % 500 == 0) {
                        System.out.println("[Importer] " + count + " movies...");
                    }
                } catch (Exception e) {
                    // Skip malformed rows without stopping the whole import
                }
            }
        }
        return idMap;
    }

    /** Reads credits.csv and links actors to already-imported movies. */
    private void importCredits(String path, Map<String, Integer> idMap)
            throws IOException, CsvValidationException {

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            Map<String, Integer> col = indexHeaders(reader.readNext());
            String[] row;

            while ((row = reader.readNext()) != null) {
                try {
                    String datasetId = safeGet(row, col, "id");
                    Integer dbMovieId = idMap.get(datasetId);
                    if (dbMovieId == null) continue;   // movie was not imported

                    for (String fullName : parseNames(safeGet(row, col, "cast"))) {
                        Actor actor = getOrCreateActor(fullName);
                        try {
                            movieDAO.addActorToMovie(dbMovieId, actor.getId());
                        } catch (SQLException ignored) {
                            // duplicate link — safe to skip
                        }
                    }
                } catch (Exception e) {
                    // Skip malformed rows
                }
            }
        }
    }

    /**
     * Parses the first genre name from the dataset's JSON-like string, e.g.:
     * [{'id': 28, 'name': 'Action'}, {'id': 12, 'name': 'Adventure'}]
     */
    private Genre parseFirstGenre(String json) throws SQLException {
        String name = extractFirstValue(json, "name");
        if (name == null || name.isBlank()) return null;
        return genreCache.computeIfAbsent(name, n -> {
            try { return genreDAO.create(n); }
            catch (SQLException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Parses all actor names from the cast JSON-like string, e.g.:
     * [{'name': 'Tom Hanks', 'character': 'Forrest'}, ...]
     */
    private List<String> parseNames(String json) {
        List<String> names = new ArrayList<>();
        int pos = 0;
        while (true) {
            String name = extractValueAfter(json, "name", pos);
            if (name == null) break;
            if (!name.isBlank()) names.add(name);
            pos = json.indexOf(name, pos) + name.length();
        }
        return names;
    }

    /** Extracts the value of the first occurrence of a key in the JSON-like string. */
    private String extractFirstValue(String json, String key) {
        return extractValueAfter(json, key, 0);
    }

    private String extractValueAfter(String json, String key, int from) {
        // Try both single and double quoted variants produced by Python's str()
        for (String pattern : new String[]{"'" + key + "': '", "\"" + key + "\": \""}) {
            int idx = json.indexOf(pattern, from);
            if (idx == -1) continue;
            int start = idx + pattern.length();
            char quote = pattern.charAt(0) == '\'' ? '\'' : '"';
            int end = json.indexOf(quote, start);
            if (end == -1) continue;
            return json.substring(start, end);
        }
        return null;
    }

    private Actor getOrCreateActor(String fullName) throws SQLException {
        return actorCache.computeIfAbsent(fullName, n -> {
            try {
                String[] parts = n.split(" ", 2);
                return actorDAO.create(parts[0], parts.length > 1 ? parts[1] : "");
            } catch (SQLException e) { throw new RuntimeException(e); }
        });
    }

    private Map<String, Integer> indexHeaders(String[] headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim().toLowerCase().replace("\"", ""), i);
        }
        return map;
    }

    private String safeGet(String[] row, Map<String, Integer> col, String key) {
        Integer idx = col.get(key);
        if (idx == null || idx >= row.length || row[idx] == null) return "";
        return row[idx].trim();
    }

    private LocalDate parseDate(String s) {
        try { return LocalDate.parse(s, DATE_FMT); }
        catch (DateTimeParseException e) { return null; }
    }

    private int parseInt(String s) {
        try { return (int) Double.parseDouble(s); }
        catch (NumberFormatException e) { return 0; }
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return 0.0; }
    }
}