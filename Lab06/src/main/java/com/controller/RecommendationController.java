package com.controller;

import com.model.Movie;
import com.service.MovieRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private MovieRecommendationService recommendationService;

    @GetMapping("/unrelated")
    public List<Movie> getUnrelated(@RequestParam int minSize) {
        try {
            return recommendationService.getUnrelatedMovies(minSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}