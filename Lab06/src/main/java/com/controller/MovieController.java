package com.controller;

import com.dao.MovieDAO;
import com.model.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieDAO movieDAO = new MovieDAO();

    @GetMapping
    public List<Movie> getMovies() {
        try {
            return movieDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping
    public void addMovie(@RequestBody Movie movie) {
        try {
            movieDAO.create(movie.getTitle(), movie.getReleaseDate(), movie.getDuration(), movie.getScore(), movie.getGenre().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PatchMapping
    public void changeMovie(@RequestBody Movie movie) {
        try {
            if (movieDAO.findById(movie.getId()).isEmpty()) {
                System.out.println("Nu am gasit filmul cu id " + movie.getId());
                return;
            }
            movieDAO.update(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping
    public void deleteMovie(@RequestBody Movie movie) {
        try {
            if (movieDAO.findById(movie.getId()).isEmpty()) {
                System.out.println("Nu am gasit filmul cu id " + movie.getId());
                return;
            }
            movieDAO.remove(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/{id}")
    public void updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        try {
            movie.setId(id);
            if (movieDAO.findById(id).isEmpty()) {
                System.out.println("Nu s-a putut face update: Filmul cu id " + id + " nu exista.");
                return;
            }
            movieDAO.update(movie);
            System.out.println("Filmul a fost inlocuit cu succes via PUT.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}