import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.IntStream;
import java.util.random.*;

public class Main {
    public static void main (String[] args){
        //COMPULSORY
//        List<Intersection> intersections = IntStream.rangeClosed(0, 9)
//                .mapToObj(i -> new Intersection("v" + i) )
//                .toList();
//
//        List<Street> streets = new LinkedList<>(IntStream.range(0,intersections.size()-1)
//                .mapToObj(i -> {
//                    return new Street(intersections.get(i), intersections.get(i+1), "s" + i, (i*31 + 9)%31);
//                })
//                .toList());
//
//        streets.sort(Comparator.comparingInt(Street::getLength));
//
//        Set<Intersection> setOfIntersections = new HashSet<>(intersections); // verificam daca elementele din set sunt unice cu metoda equals, care este overriten in clasa Intersection
//
//
//        System.out.println(setOfIntersections.size() - intersections.size());
        //HOMEWORK
//        System.out.println();
//        City city = new City();
//        Faker faker = new Faker();
//
//        Intersection intersection1 = new Intersection(faker.address().cityName());
//        Intersection intersection2 = new Intersection(faker.address().cityName());
//        Intersection intersection3 = new Intersection(faker.address().cityName());
//        Intersection intersection4 = new Intersection(faker.address().cityName());
//        Intersection intersection5 = new Intersection(faker.address().cityName());
//
//        city.addIntersection(intersection1);
//        city.addIntersection(intersection2);
//        city.addIntersection(intersection3);
//        city.addIntersection(intersection4);
//        city.addIntersection(intersection5);
//
//        city.addStreet(intersection1, intersection2, 10);
//        city.addStreet(intersection2, intersection4, 15);
//        city.addStreet(intersection1, intersection3, 5);
//        city.addStreet(intersection3, intersection4, 30);
//        city.addStreet(intersection1, intersection4, 50);
//        city.addStreet(intersection1, intersection5, 50);
//
//        city.displayAllStreets();
//        city.displayGoofyStreets(30);
//        CitySolver.displayTopRoutes(city, intersection1, intersection4, 3);

        //ADVANCED
        City city = new City();
        CityGenerator generator = new CityGenerator();
        System.out.println("Generating city layout...");
        generator.populateCity(city, 20);
        System.out.println("City created with " + city.getIntersections().size() +
                " intersections and " + city.getStreets().size() + " streets.");
        System.out.println();

        MaintenanceSolver solver = new MaintenanceSolver();

        List<Street> maintenanceRoute = solver.getMaintenanceRoute(city);
        System.out.println("Maintenance Route:");
        int totalDistance = 0;

        city.displayAllStreets();

        for (int i = 0; i < maintenanceRoute.size(); i++) {
            Street s = maintenanceRoute.get(i);
            totalDistance += s.getLength();

            System.out.println((i+1) + ". " + s.getName());
        }
        System.out.println("Total distance of maintenance route: " + totalDistance);
    }
}
