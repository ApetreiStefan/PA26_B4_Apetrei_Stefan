package com.partition;

import com.model.Movie;

import java.util.*;

/**
 * Partitions a collection of movies into lists where:
 *   1. No two movies in the same list share an actor (unrelated movies only).
 *   2. The number of lists is as small as possible.
 *   3. Any two lists differ in size by at most 1 (balanced).
 *
 * This is equivalent to graph coloring: movies are nodes, an edge connects
 * two movies that share an actor, and each "color" is one output list.
 * The minimum number of colors needed equals the chromatic number of the graph,
 * which is at least the size of the largest clique (largest set of mutually
 * related movies).
 *
 * Strategy:
 *   Phase 1 — Greedy coloring with the DSatur heuristic to get a good
 *              initial number of lists (colors).
 *   Phase 2 — Balance the lists so they differ by at most 1 in size.
 *              Movies are moved between lists only if doing so does not
 *              violate the "unrelated" constraint.
 */
public class MoviePartitioner {

    /**
     * @param movies list of movies, each already having their actors loaded
     * @return a list of movie lists, each containing only mutually unrelated movies
     */
    public List<List<Movie>> partition(List<Movie> movies) {
        if (movies.isEmpty()) return Collections.emptyList();

        // Build adjacency: movieId → set of related movieIds
        Map<Integer, Set<Integer>> adj = buildAdjacency(movies);
        Map<Integer, Movie> byId = new HashMap<>();
        for (Movie m : movies) byId.put(m.getId(), m);

        // Phase 1: DSatur greedy coloring
        // movieId → assigned color (list index)
        Map<Integer, Integer> coloring = dsatur(movies, adj);

        // Collect into lists
        int numColors = coloring.values().stream().mapToInt(i -> i).max().orElse(0) + 1;
        List<List<Movie>> lists = new ArrayList<>();
        for (int i = 0; i < numColors; i++) lists.add(new ArrayList<>());
        for (Movie m : movies) lists.get(coloring.get(m.getId())).add(m);

        // Phase 2: Balance lists so max size - min size <= 1
        balance(lists, adj);

        // Sort each list by title for a clean output
        for (List<Movie> list : lists) {
            list.sort(Comparator.comparing(Movie::getTitle));
        }

        return lists;
    }

    /**
     * DSatur assigns colors in order of decreasing "saturation" — the number
     * of distinct colors already used by a node's neighbors. Ties are broken
     * by degree. This tends to produce near-optimal colorings in practice.
     */
    private Map<Integer, Integer> dsatur(List<Movie> movies, Map<Integer, Set<Integer>> adj) {
        Map<Integer, Integer> color = new HashMap<>();  // movieId → color
        Map<Integer, Set<Integer>> adjColors = new HashMap<>(); // movieId → neighbor colors

        for (Movie movie : movies) adjColors.put(movie.getId(), new HashSet<>());

        // Priority queue ordered by (saturation DESC, degree DESC)
        PriorityQueue<Movie> priorityQueue = new PriorityQueue<>((a, b) -> {
            int satA = adjColors.get(a.getId()).size();
            int satB = adjColors.get(b.getId()).size();
            if (satB != satA) return satB - satA;
            return adj.get(b.getId()).size() - adj.get(a.getId()).size();
        });
        priorityQueue.addAll(movies);

        Set<Movie> uncolored = new HashSet<>(movies);

        while (!uncolored.isEmpty()) {
            // Pick uncolored node with highest saturation (re-scan since PQ is stale)
            Movie node = uncolored.stream().max(Comparator
                    .comparingInt((Movie m) -> adjColors.get(m.getId()).size())
                    .thenComparingInt(m -> adj.get(m.getId()).size())
            ).orElseThrow();

            // Assign the smallest color not used by any neighbor
            Set<Integer> usedByNeighbors = adjColors.get(node.getId());
            int c = 0;
            while (usedByNeighbors.contains(c)) c++;
            color.put(node.getId(), c);

            // Update saturation of neighbors
            for (int neighborId : adj.get(node.getId())) {
                adjColors.get(neighborId).add(c);
            }

            uncolored.remove(node);
        }
        return color;
    }

    /**
     * Moves movies from oversized lists to undersized ones, provided the
     * move does not create a conflict (shared actor) in the destination list.
     *
     * Target: every list has size floor(n/k) or ceil(n/k),
     * where n = total movies, k = number of lists.
     */
    private void balance(List<List<Movie>> lists, Map<Integer, Set<Integer>> adj) {
        int total    = lists.stream().mapToInt(List::size).sum();
        int k        = lists.size();
        int minSize  = total / k;
        int maxSize  = minSize + (total % k == 0 ? 0 : 1);

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int from = 0; from < lists.size(); from++) {
                if (lists.get(from).size() <= maxSize) continue;

                // This list is too large — try to move a movie to a smaller list
                for (int to = 0; to < lists.size(); to++) {
                    if (lists.get(to).size() >= minSize) continue;

                    // Find a movie in 'from' that has no conflict in 'to'
                    for (int mi = 0; mi < lists.get(from).size(); mi++) {
                        Movie candidate = lists.get(from).get(mi);
                        if (canPlace(candidate, lists.get(to), adj)) {
                            lists.get(to).add(lists.get(from).remove(mi));
                            changed = true;
                            break;
                        }
                    }
                    if (lists.get(from).size() <= maxSize) break;
                }
            }
        }
    }

    /** Returns true if placing {@code movie} into {@code list} creates no conflicts. */
    private boolean canPlace(Movie movie, List<Movie> list, Map<Integer, Set<Integer>> adj) {
        Set<Integer> related = adj.get(movie.getId());
        for (Movie movieIt : list) {
            if (related.contains(movieIt.getId())) return false;
        }
        return true;
    }

    private Map<Integer, Set<Integer>> buildAdjacency(List<Movie> movies) {
        Map<Integer, Set<Integer>> adj = new HashMap<>();
        for (Movie m : movies) adj.put(m.getId(), new HashSet<>());

        for (int i = 0; i < movies.size(); i++) {
            for (int j = i + 1; j < movies.size(); j++) {
                Movie movie1 = movies.get(i);
                Movie movie2 = movies.get(j);
                if (movie1.isRelatedTo(movie2)) {
                    adj.get(movie1.getId()).add(movie2.getId());
                    adj.get(movie2.getId()).add(movie1.getId());
                }
            }
        }
        return adj;
    }
}