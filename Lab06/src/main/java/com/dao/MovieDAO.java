package com.dao;

import com.db.DatabaseConnection;
import com.model.Actor;
import com.model.Genre;
import com.model.Movie;
import com.model.MovieReportEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public Movie create(String title, LocalDate releaseDate, int duration,
                        double score, int genreId) throws SQLException {
        String sql = """
                INSERT INTO movies (title, release_date, duration, score, genre_id)
                VALUES (?, ?, ?, ?, ?) RETURNING id
                """;
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(releaseDate));
            ps.setInt(3, duration);
            ps.setDouble(4, score);
            ps.setInt(5, genreId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt("id");
            // Fetch the genre to build a complete Movie object
            Genre genre = new Genre(genreId, null);
            System.out.println("Adaugat-am un film (daca nu era deja in tabel)");
            return new Movie(id, title, releaseDate, duration, score, genre);
        }
    }

    // Links an actor to a movie via the junction table
    public void addActorToMovie(int movieId, int actorId) throws SQLException {
        String sql = "INSERT INTO movie_actor (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, actorId);
            ps.executeUpdate();
        }
    }

    public Optional<Movie> findById(int id) throws SQLException {
        String sql = """
                SELECT m.id, m.title, m.release_date, m.duration, m.score,
                       g.id AS genre_id, g.name AS genre_name
                FROM movies m
                LEFT JOIN genres g ON g.id = m.genre_id
                WHERE m.id = ?
                """;
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            Movie movie = mapRow(rs);
            loadActors(movie);
            return Optional.of(movie);
        }
    }

    public List<Movie> findAll() throws SQLException {
        String sql = """
                SELECT m.id, m.title, m.release_date, m.duration, m.score,
                       g.id AS genre_id, g.name AS genre_name
                FROM movies m
                LEFT JOIN genres g ON g.id = m.genre_id
                ORDER BY m.title
                """;
        List<Movie> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Movie movie = mapRow(rs);
                loadActors(movie);
                list.add(movie);
            }
        }
        return list;
    }

    // Reads from the movies_report view — used by the HTML com.report generator
    public List<MovieReportEntry> findAllForReport() throws SQLException {
        String sql = "SELECT title, release_date, duration, score, genre, actors FROM movies_report";
        List<MovieReportEntry> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MovieReportEntry(
                        rs.getString("title"),
                        rs.getString("release_date"),
                        rs.getInt("duration"),
                        rs.getDouble("score"),
                        rs.getString("genre"),
                        rs.getString("actors")
                ));
            }
        }
        return list;
    }
    private Movie mapRow(ResultSet rs) throws SQLException {
        Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
        Date releaseDate = rs.getDate("release_date");
        return new Movie(
                rs.getInt("id"),
                rs.getString("title"),
                releaseDate != null ? releaseDate.toLocalDate() : null,
                rs.getInt("duration"),
                rs.getDouble("score"),
                genre
        );
    }

    // Fetches actors for a movie and attaches them to the Movie object
    private void loadActors(Movie movie) throws SQLException {
        String sql = """
                SELECT a.id, a.first_name, a.last_name
                FROM actors a
                JOIN movie_actor ma ON ma.actor_id = a.id
                WHERE ma.movie_id = ?
                """;
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movie.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                movie.addActor(new Actor(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
            }
        }
    }
}