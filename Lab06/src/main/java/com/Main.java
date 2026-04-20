package com;

import com.dao.ActorDAO;
import com.dao.GenreDAO;
import com.dao.MovieDAO;
import com.dao.MovieListDAO;
import com.db.DatabaseConnection;
import com.importer.MovieLensImporter;
import com.model.Movie;
import com.model.MovieList;
import com.model.MovieReportEntry;
import com.partition.MoviePartitioner;
import com.report.ReportGenerator;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        GenreDAO genreDAO = new GenreDAO();
        ActorDAO actorDAO = new ActorDAO();
        MovieDAO movieDAO = new MovieDAO();
        MovieListDAO listDAO = new MovieListDAO();

        System.out.println("=== Importing dataset ===");
        MovieLensImporter importer = new MovieLensImporter(genreDAO, actorDAO, movieDAO);
        importer.importData(
                "movies_metadata.csv",   // path to movies_metadata.csv
                "credits.csv",           // path to credits.csv
                200                      // import first 200 movies for testing
        );

        System.out.println("\n=== Partitioning movies ===");
        List<Movie> allMovies = movieDAO.findAll();
        System.out.println("Total movies loaded: " + allMovies.size());

        MoviePartitioner partitioner = new MoviePartitioner();
        List<List<Movie>> partitions = partitioner.partition(allMovies);

        System.out.println("Number of lists produced: " + partitions.size());
        for (int i = 0; i < partitions.size(); i++) {
            System.out.println("  List " + (i + 1) + ": " + partitions.get(i).size() + " movies");
        }

        System.out.println("\n=== Saving lists to database ===");
        for (int i = 0; i < partitions.size(); i++) {
            MovieList list = listDAO.create("Unrelated Group " + (i + 1));
            for (Movie movie : partitions.get(i)) {
                listDAO.addMovieToList(list.getId(), movie.getId());
            }
            System.out.println("Saved: " + list);
        }

        System.out.println("\n=== Generating HTML com.report ===");
        List<MovieReportEntry> reportData = movieDAO.findAllForReport();
        new ReportGenerator().generate(reportData, "com.report.html");

        DatabaseConnection.getInstance().shutdown();
    }
}