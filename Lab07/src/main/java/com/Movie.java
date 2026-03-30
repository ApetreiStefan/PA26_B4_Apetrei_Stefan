package com;

import lombok.Getter;

@Getter
public class Movie {
    private int id;
    private int year;
    private String title;
    private String genre;

    public Movie(int id, String title, String genre, int year) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
    }
}