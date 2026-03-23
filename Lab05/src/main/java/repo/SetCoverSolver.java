package repo;

import java.util.*;

/**
 * Solves the Set Cover problem:
 *   Given a universe of concepts C and a repository of resources (each covering
 *   a subset of C), find the smallest sub-collection of resources whose union
 *   of concept sets equals C.
 *
 * SET COVER IS NP-HARD, so we use the classic greedy approximation
 * (guaranteed within ln|C|+1 of optimum) together with an exact branch-and-bound
 * search for small instances.
 */
public class SetCoverSolver {

    public static List<Resource> greedyCover(Repository repository, Set<Concept> universe) {

        Set<Concept> remaining = EnumSet.copyOf(universe);
        List<Resource> chosen = new ArrayList<>();
        List<Resource> pool = new ArrayList<>(repository.getResources());

        while (!remaining.isEmpty()) {
            Resource best = null;
            int bestCovered = -1;

            for (Resource resource : pool) {
                if (chosen.contains(resource)) continue;
                int covered = countIntersection(resource.getConcepts(), remaining);
                if (covered > bestCovered) {
                    bestCovered = covered;
                    best = resource;
                }
            }

            if (best == null || bestCovered == 0) break; // universe cannot be covered
            chosen.add(best);
            remaining.removeAll(best.getConcepts());
        }

        return remaining.isEmpty() ? chosen : null; // null = infeasible
    }

    public static List<Resource> exactMinimumCover(
            Repository repository, Set<Concept> universe) {

        List<Resource> resources = new ArrayList<>(repository.getResources());
        int n = resources.size();

        List<Resource> greedySolution = greedyCover(repository, universe);
        int[] bestSize = { greedySolution != null ? greedySolution.size() : n + 1 };
        List<Resource>[] best = new List[]{ greedySolution };

        branchAndBound(resources, universe, new ArrayList<>(), EnumSet.noneOf(Concept.class), 0, best, bestSize);

        return best[0];
    }

    /**
     * The recursive core of the problem
     * It takes a resource or it does not
     */
    private static void branchAndBound(
            List<Resource> resources,
            Set<Concept> universe,
            List<Resource> current,
            Set<Concept> covered,
            int idx,
            List<Resource>[] best,
            int[] bestSize) {

        if (covered.containsAll(universe)) {
            if (current.size() < bestSize[0]) {
                bestSize[0] = current.size();
                best[0] = new ArrayList<>(current);
            }
            return;
        }
        if (idx >= resources.size()) return;
        if (current.size() >= bestSize[0] - 1) return;

        Set<Concept> remaining = EnumSet.copyOf(universe);
        remaining.removeAll(covered);
        Set<Concept> reachable = EnumSet.noneOf(Concept.class);
        for (int i = idx; i < resources.size(); i++) {
            reachable.addAll(resources.get(i).getConcepts());
        }
        if (!reachable.containsAll(remaining)) return;

        Resource r = resources.get(idx);

        Set<Concept> newCovered = EnumSet.copyOf(covered);
        newCovered.addAll(r.getConcepts());
        current.add(r);
        branchAndBound(resources, universe, current, newCovered, idx + 1, best, bestSize);
        current.remove(current.size() - 1);

        branchAndBound(resources, universe, current, covered, idx + 1, best, bestSize);
    }

    public static Repository generateRandomInstance(
            int numResources, int numConcepts, double conceptDensity, Random rng) {

        Concept[] all = Concept.values();
        Concept[] subset = Arrays.copyOf(all, Math.min(numConcepts, all.length));

        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < numResources; i++) {
            Set<Concept> concepts = EnumSet.noneOf(Concept.class);
            for (Concept c : subset) {
                if (rng.nextDouble() < conceptDensity) concepts.add(c);
            }
            resources.add(new Resource(
                    "r" + i,
                    "Resource " + i,
                    "/data/r" + i + ".pdf",
                    String.valueOf(2000 + rng.nextInt(24)),
                    "Author " + (char)('A' + rng.nextInt(26)),
                    concepts
            ));
        }
        return new Repository(resources);
    }

    public static void runBenchmark() {
        Random rng = new Random(42);
        Set<Concept> universe = EnumSet.allOf(Concept.class);

        System.out.println("=== Set Cover Benchmark ===\n");
        System.out.printf("%-12s %-10s %-10s %-14s %-14s %-12s%n",
                "Resources", "Concepts", "Density", "Greedy size", "Exact size", "Time (ms)");
        System.out.println("-".repeat(76));

        int[][] configs = {
                {10, 5, 0}, {20, 8, 1}, {30, 10, 2}, {50, 10, 3}, {100, 10, 4}
        };
        double[] densities = {0.3, 0.4, 0.5, 0.6, 0.7};

        for (int[] cfg : configs) {
            int numRes = cfg[0], numCon = cfg[1];
            double density = densities[cfg[2]];
            Repository repo = generateRandomInstance(numRes, numCon, density, rng);

            Set<Concept> usedConcepts = EnumSet.noneOf(Concept.class);
            repo.getResources().forEach(r -> usedConcepts.addAll(r.getConcepts()));
            if (usedConcepts.isEmpty()) continue;

            long start = System.currentTimeMillis();
            List<Resource> greedy = greedyCover(repo, usedConcepts);
            int greedySize = greedy != null ? greedy.size() : -1;

            List<Resource> exact = null;
            if (numRes <= 30) {
                exact = exactMinimumCover(repo, usedConcepts);
            }
            long elapsed = System.currentTimeMillis() - start;

            String exactStr = exact != null ? String.valueOf(exact.size()) : "skipped";
            System.out.printf("%-12d %-10d %-10.1f %-14d %-14s %-12d%n",
                    numRes, numCon, density, greedySize, exactStr, elapsed);
        }
    }

    private static int countIntersection(Set<Concept> a, Set<Concept> b) {
        int count = 0;
        for (Concept c : a) if (b.contains(c)) count++;
        return count;
    }
}
