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

    @PatchMapping
    public void changeMovie(@RequestBody Movie movie){
        try{
            if(movieDAO.findById(movie.getId())==null){
                System.out.println("Nu am gasit filmul cu id" + movie.getId());
                return;
            }
            movieDAO.update(movie);
        }
        catch(Exception e){e.printStackTrace();}
    }

    @DeleteMapping
    public void deleteMovie(@RequestBody Movie movie){
        try{
            if(movieDAO.findById(movie.getId())==null){
                System.out.println("Nu am gasit filmul cu id" + movie.getId());
                return;
            }
            movieDAO.remove(movie);
        }
        catch(Exception e){e.printStackTrace();}
    }
}
// Mai bine ma faceam cioban oare?
//{
//        "id": 101,
//        "title": "Inception",
//        "releaseDate": "2010-07-16",
//        "duration": 148,
//        "score": 8.8,
//        "genre": {
//        "id": 1
//        },
//        "actors": [
//        { "id": 5, "name": "Leonardo DiCaprio" },
//        { "id": 12, "name": "Cillian Murphy" }
//        ]
//        }