package com.dao;

import com.db.DatabaseConnection;
import com.model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public Genre create(String name) throws SQLException {
        String sql = "INSERT INTO genres (name) VALUES (?) ON CONFLICT (name) DO NOTHING RETURNING id";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Genre(rs.getInt("id"), name);  // freshly inserted
            } else {
                return findByName(name).orElseThrow();    // already existed, look it up
            }
        }
    }

    public Optional<Genre> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next()
                    ? Optional.of(new Genre(rs.getInt("id"), rs.getString("name")))
                    : Optional.empty();
        }
    }

    public Optional<Genre> findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM genres WHERE name = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next()
                    ? Optional.of(new Genre(rs.getInt("id"), rs.getString("name")))
                    : Optional.empty();
        }
    }

    public List<Genre> findAll() throws SQLException {
        String sql = "SELECT id, name FROM genres ORDER BY name";
        List<Genre> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Genre(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }
}