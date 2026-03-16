import org.jgrapht.Graph;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.GraphPath;

import java.util.List;

public class MaintenanceSolver {

    /**
     * Finds a maintenance route that visits all intersections.
     * Runs in O(E log V) time.
     */
    public List<Street> getMaintenanceRoute(City city) {
        Graph<Intersection, Street> graph = new SimpleWeightedGraph<>(Street.class);
        city.getIntersections().forEach(graph::addVertex);
        city.getStreets().forEach(s -> {
            graph.addEdge(s.getFirst(), s.getSecond(), s);
            graph.setEdgeWeight(s, s.getLength());
        });
        TwoApproxMetricTSP<Intersection, Street> tsp = new TwoApproxMetricTSP<>();
        GraphPath<Intersection, Street> tour = tsp.getTour(graph);

        return tour.getEdgeList();
    }
}