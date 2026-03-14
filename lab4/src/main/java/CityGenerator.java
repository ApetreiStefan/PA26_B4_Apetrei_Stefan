import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;

public class CityGenerator {
    private static final Faker faker = new Faker();

    /**
     * Generates a random city where street lengths satisfy the triangle inequality.
     * Use this to test the 2-approximation TSP algorithm.
     */
    public void populateCity(City city, int numIntersections) {
        Map<Intersection, int[]> coordinates = new HashMap<>();

        for (int i = 0; i < numIntersections; i++) {
            String name = faker.address().cityName();
            Intersection intersection = new Intersection(name);

            city.addIntersection(intersection);
            int x = (int) faker.number().randomNumber(3,false);
            int y = (int) faker.number().randomNumber(3, false);
            coordinates.put(intersection, new int[]{x, y});
        }

        // How do we ensure the car can always find a path?
        // We add n(n-1) /2 edges :p
        for (int i = 0; i < city.getIntersections().size(); i++) {
            for (int j = i + 1; j < city.getIntersections().size(); j++) {
                Intersection u = city.getIntersections().get(i);
                Intersection v = city.getIntersections().get(j);

                int[] coordU = coordinates.get(u);
                int[] coordV = coordinates.get(v);

                // Euclidean Distance: sqrt((x2-x1)^2 + (y2-y1)^2)
                double distance = Math.sqrt(
                        Math.pow(coordU[0] - coordV[0], 2) +
                        Math.pow(coordU[1] - coordV[1], 2)
                );
                city.addStreet(u, v, (int) distance);
            }
        }
    }
}