package com;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @GetMapping
    public List<Movie> getMovies() {
        return Arrays.asList(
                new Movie(1, "Inception", "Sci-Fi", 2010),
                new Movie(2, "The Dark Knight", "Action", 2008),
                new Movie(3, "Interstellar", "Sci-Fi", 2014)
        );
    }
}