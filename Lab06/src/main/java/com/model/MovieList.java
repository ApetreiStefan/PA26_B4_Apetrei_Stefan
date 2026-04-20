package com.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private int id;
    private String name;
    private OffsetDateTime createdAt;
    private List<Movie> movies;

    public MovieList(int id, String name, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.movies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addMovie(Movie m) {
        movies.add(m);
    }

    public int size() {
        return movies.size();
    }

    @Override
    public String toString() {
        return "MovieList{id=" + id + ", name='" + name + "', movies=" + movies.size() + "}";
    }
}