import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class CitySolver {

    /**
     * Finds the top N shortest paths between two intersections.
     * @param city The city object containing intersections and streets.
     * @param start The starting intersection.
     * @param end The destination intersection.
     * @param k The number of solutions to return.
     * @return A list of paths (lists of streets) ordered by total length.
     */
    public List<List<Street>> getKShortestRoutes(City city, Intersection start, Intersection end, int k) {
        Graph<Intersection, Street> graph = new SimpleWeightedGraph<>(Street.class);
        for (Intersection i : city.getIntersections()) {
            graph.addVertex(i);
        }
        for (Street s : city.getStreets()) {
            graph.addEdge(s.getFirst(), s.getSecond(), s);
            graph.setEdgeWeight(s, (double) s.getLength());
        }
        YenKShortestPath<Intersection, Street> yenAlgorithm = new YenKShortestPath<>(graph);
        return yenAlgorithm.getPaths(start, end, k)
                .stream()
                .map(GraphPath::getEdgeList)
                .collect(Collectors.toList());
    }

    public static void displayTopRoutes(City city, Intersection start, Intersection end, int k) {
        CitySolver solver = new CitySolver();
        List<List<Street>> solutions = solver.getKShortestRoutes(city, start, end, k);

        System.out.println("--- Top " + solutions.size() + " shortest paths from " +
                start.getName() + " to " + end.getName() + " ---");

        for (int i = 0; i < solutions.size(); i++) {
            List<Street> path = solutions.get(i);
            int totalCost = path.stream().mapToInt(Street::getLength).sum();

            String routeDescription = path.stream()
                    .map(Street::getName)
                    //This collects the stream of names into a single stream, "joining" them with "->"
                    .collect(java.util.stream.Collectors.joining(" -> "));

            System.out.println("Solution " + (i + 1) + " [Cost: " + totalCost + "]:" + routeDescription);
        }
    }
}