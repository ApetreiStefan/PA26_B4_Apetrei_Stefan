package com.service;

import com.dao.MovieDAO;
import com.model.Movie;
import com.model.Actor;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieRecommendationService {

    private final MovieDAO movieDAO = new MovieDAO();

    public List<Movie> getUnrelatedMovies(int minSize) throws Exception {
        List<Movie> allMovies = movieDAO.findAll();
        int n = allMovies.size();

        Model model = new Model("Unrelated Movies Solver");

        // selection[i] is 1 if we pick movie i, 0 otherwise
        BoolVar[] selection = model.boolVarArray("selection", n);

        // Constraint 1: Number of movies picked must be > minSize
        model.sum(selection, ">", minSize).post();

        // Constraint 2: Unrelated (No shared actors)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (shareActors(allMovies.get(i), allMovies.get(j))) {
                    // Cannot pick both: selection[i] + selection[j] <= 1
                    model.arithm(selection[i], "+", selection[j], "<=", 1).post();
                }
            }
        }

        if (model.getSolver().solve()) {
            List<Movie> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (selection[i].getValue() == 1) {
                    result.add(allMovies.get(i));
                }
            }
            return result;
        }

        return new ArrayList<>(); // No solution found
    }

    private boolean shareActors(Movie m1, Movie m2) {
        Set<Integer> ids1 = m1.getActors().stream().map(Actor::getId).collect(Collectors.toSet());
        for (Actor a : m2.getActors()) {
            if (ids1.contains(a.getId())) return true;
        }
        return false;
    }
}