import dao.ActorDAO;
import dao.GenreDAO;
import dao.MovieDAO;
import db.DatabaseConnection;
import model.*;
import report.ReportGenerator;

//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public class Main {
//
//    public static void main(String[] args) throws Exception {
//
//        GenreDAO genreDAO = new GenreDAO();
//        ActorDAO actorDAO = new ActorDAO();
//        MovieDAO movieDAO = new MovieDAO();
//
//        // -- Genres --
//        System.out.println("=== Genres ===");
//        Genre scifi  = genreDAO.create("Sci-Fi");
//        Genre drama  = genreDAO.create("Drama");
//        Genre comedy = genreDAO.create("Comedy");
//        System.out.println("Created: " + scifi);
//        System.out.println("Created: " + drama);
//
//        Optional<Genre> found = genreDAO.findById(scifi.getId());
//        System.out.println("findById -> " + found);
//
//        Optional<Genre> foundByName = genreDAO.findByName("Drama");
//        System.out.println("findByName -> " + foundByName);
//
//        System.out.println("All genres: " + genreDAO.findAll());
//
//        // -- Actors --
//        System.out.println("\n=== Actors ===");
//        Actor keanu = actorDAO.create("Keanu", "Reeves");
//        Actor carrie = actorDAO.create("Carrie-Anne", "Moss");
//        Actor tom = actorDAO.create("Tom", "Hanks");
//        System.out.println("Created: " + keanu);
//        System.out.println("Created: " + carrie);
//        System.out.println("All actors: " + actorDAO.findAll());
//
//        // -- Movies --
//        System.out.println("\n=== Movies ===");
//        Movie matrix = movieDAO.create("The Matrix", LocalDate.of(1999, 3, 31), 136, 8.7, scifi.getId());
//        Movie castAway = movieDAO.create("Cast Away", LocalDate.of(2000, 12, 22), 143, 7.8, drama.getId());
//        System.out.println("Created: " + matrix);
//        System.out.println("Created: " + castAway);
//
//        // -- Link actors to movies --
//        movieDAO.addActorToMovie(matrix.getId(),  keanu.getId());
//        movieDAO.addActorToMovie(matrix.getId(),  carrie.getId());
//        movieDAO.addActorToMovie(castAway.getId(), tom.getId());
//
//        // -- Fetch full movie with actors --
//        System.out.println("\n=== Full movie details ===");
//        Optional<Movie> fullMovie = movieDAO.findById(matrix.getId());
//        fullMovie.ifPresent(m -> {
//            System.out.println(m);
//            System.out.println("  Genre:  " + m.getGenre().getName());
//            System.out.println("  Actors: " + m.getActors().stream()
//                    .map(Actor::getFullName).toList());
//        });
//
//        // -- HTML Report --
//        System.out.println("\n=== Generating HTML report ===");
//        List<MovieReportEntry> reportData = movieDAO.findAllForReport();
//        new ReportGenerator().generate(reportData, "report.html");
//
//        DatabaseConnection.getInstance().shutdown();
//    }
//}

import dao.ActorDAO;
import dao.GenreDAO;
import dao.MovieDAO;
import dao.MovieListDAO;
import db.DatabaseConnection;
import importer.MovieLensImporter;
import model.Movie;
import model.MovieList;
import model.MovieReportEntry;
import partition.MoviePartitioner;
import report.ReportGenerator;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        GenreDAO     genreDAO = new GenreDAO();
        ActorDAO     actorDAO = new ActorDAO();
        MovieDAO     movieDAO = new MovieDAO();
        MovieListDAO listDAO  = new MovieListDAO();

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

        System.out.println("\n=== Generating HTML report ===");
        List<MovieReportEntry> reportData = movieDAO.findAllForReport();
        new ReportGenerator().generate(reportData, "report.html");

        DatabaseConnection.getInstance().shutdown();
    }
}