package com.controller;

import com.dao.MovieDAO;
import com.model.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    MovieDAO movieDAO = new MovieDAO();

    @GetMapping
    public List<Movie> getMovies() {
               try{
            return movieDAO.findAll();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping
    public void addMovie(@RequestBody Movie movie){
        try {
            movieDAO.create(movie.getTitle(), movie.getReleaseDate(), movie.getDuration(), movie.getScore(), movie.getGenre().getId());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}