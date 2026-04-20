package com.client;

import com.model.Genre;
import com.model.Movie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;

public class MovieClient {

    private static final String BASE_URL = "http://localhost:8081/movies";
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        MovieClient client = new MovieClient();

        Movie newMovie = new Movie(1, "Inception", LocalDate.of(2010, 7, 16), 148, 8.8, new Genre(1, "Action"));
        client.addMovie(newMovie);

        client.printAllMovies();

        //newMovie.setScore(9.0);
        //client.updateMovie(newMovie); //Biblioteca antica, PATCH nu exista

        client.deleteMovie(newMovie);
    }

    public void addMovie(Movie movie) {
        System.out.println("Sending POST request...");
        restTemplate.postForEntity(BASE_URL, movie, Void.class);
        System.out.println("Movie added successfully!");
    }

    public void printAllMovies() {
        System.out.println("Sending GET request...");
        Movie[] movies = restTemplate.getForObject(BASE_URL, Movie[].class);
        if (movies != null) {
            Arrays.stream(movies).forEach(m -> System.out.println("Found: " + m.getTitle()));
        }
    }

    public void updateMovie(Movie movie) {
        System.out.println("Sending PATCH request...");
        HttpEntity<Movie> requestEntity = new HttpEntity<>(movie);
        restTemplate.exchange(BASE_URL, HttpMethod.PATCH, requestEntity, Void.class);
        System.out.println("Movie updated successfully!");
    }

    public void deleteMovie(Movie movie) {
        System.out.println("Sending DELETE request...");
        HttpEntity<Movie> requestEntity = new HttpEntity<>(movie);
        restTemplate.exchange(BASE_URL, HttpMethod.DELETE, requestEntity, Void.class);
        System.out.println("Movie deleted successfully!");
    }
}