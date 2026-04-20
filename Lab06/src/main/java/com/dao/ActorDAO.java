package com.dao;

import com.db.DatabaseConnection;
import com.model.Actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public Actor create(String firstName, String lastName) throws SQLException {
        String sql = "INSERT INTO actors (first_name, last_name) VALUES (?, ?) RETURNING id";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new Actor(rs.getInt("id"), firstName, lastName);
        }
    }

    public Optional<Actor> findById(int id) throws SQLException {
        String sql = "SELECT id, first_name, last_name FROM actors WHERE id = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
        }
    }

    public List<Actor> findAll() throws SQLException {
        String sql = "SELECT id, first_name, last_name FROM actors ORDER BY last_name";
        List<Actor> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // Returns all actors that appear in a given movie
    public List<Actor> findByMovieId(int movieId) throws SQLException {
        String sql = """
                SELECT a.id, a.first_name, a.last_name
                FROM actors a
                JOIN movie_actor ma ON ma.actor_id = a.id
                WHERE ma.movie_id = ?
                ORDER BY a.last_name
                """;
        List<Actor> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Actor mapRow(ResultSet rs) throws SQLException {
        return new Actor(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}