package com.dao;

import com.db.DatabaseConnection;
import com.model.MovieList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieListDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public MovieList create(String name) throws SQLException {
        String sql = "INSERT INTO movie_list (name) VALUES (?) RETURNING id, created_at";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new MovieList(
                    rs.getInt("id"),
                    name,
                    rs.getObject("created_at", java.time.OffsetDateTime.class)
            );
        }
    }

    // Adds a movie to a list via the junction table
    public void addMovieToList(int listId, int movieId) throws SQLException {
        String sql = "INSERT INTO movie_list_entry (list_id, movie_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listId);
            ps.setInt(2, movieId);
            ps.executeUpdate();
        }
    }

    public Optional<MovieList> findById(int id) throws SQLException {
        String sql = "SELECT id, name, created_at FROM movie_list WHERE id = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(new MovieList(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getObject("created_at", java.time.OffsetDateTime.class)
            ));
        }
    }

    public List<MovieList> findAll() throws SQLException {
        String sql = "SELECT id, name, created_at FROM movie_list ORDER BY created_at";
        List<MovieList> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MovieList(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getObject("created_at", java.time.OffsetDateTime.class)
                ));
            }
        }
        return list;
    }
}