package com.model;

import com.dao.GenreDAO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class Movie {
    private int id;
    private String title;
    private LocalDate releaseDate;
    private int duration;
    private double score;
    private Genre genre;
    private List<Actor> actors;

    public Movie(int id, String title, LocalDate releaseDate, int duration, double score, Genre genre) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genre = genre;
        this.actors = new ArrayList<>();
    }

    public Movie(int id, String title, LocalDate releaseDate, int duration, double score, int genreID) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        GenreDAO genreDAO = new GenreDAO();
        try {
            this.genre = genreDAO.findById(genreID).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.actors = new ArrayList<>();
    }

    public Movie() {
        actors = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public double getScore() {
        return score;
    }

    public Genre getGenre() {
        return genre;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addActor(Actor a) {
        actors.add(a);
    }

    // Two movies are related if they share at least one actor
    public boolean isRelatedTo(Movie other) {
        for (Actor a : this.actors) {
            for (Actor b : other.actors) {
                if (a.getId() == b.getId()) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Movie{id=" + id + ", title='" + title + "', score=" + score + "}";
    }
}