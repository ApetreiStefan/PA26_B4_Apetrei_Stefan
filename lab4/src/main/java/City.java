
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public class City {

    private List<Intersection> intersections = new ArrayList<>();
    private List<Street> streets = new ArrayList<>();
    private Map<Intersection, List<Street>>  adjacencies = new HashMap<>();
    private Faker faker = new Faker();

    public City(){

    }

    public void addIntersection(Intersection intersection){
        intersections.add(intersection);
        adjacencies.put(intersection,new ArrayList<>());
    }

    /**
     * addStreet() adds a new street into the city
     * The street must tie 2 intersections together
     * The name is randomly generated with java faker
     * @param first first intersection
     * @param second second intersection
     * @param length the length of the street
     */
    public void addStreet(Intersection first, Intersection second, int length){
        Street street = new Street(first, second, faker.address().streetName(), length);
        streets.add(street);
        adjacencies.get(street.getFirst()).add(street);
        adjacencies.get(street.getSecond()).add(street);
    }

    /**
     * displayGoofyStreets() is a function that displays on screen all streets
     * that have a min length and have at least 2 neighboring streets (to make for easier testing)
     * It uses the Java Stream API to do this (as asked in the Homework section)
     * @param minLength this is the minimum length of the street
     */
    void displayGoofyStreets(int minLength){
        System.out.println("Goofy Streets:");
        streets.stream().filter(street -> street.getLength() >= minLength)
                .filter(street -> adjacencies.get(street.getFirst()).size() >= 4 ||  adjacencies.get(street.getSecond()).size() >= 4)
                .forEach(street -> System.out.println(street.getName()));
        System.out.println();
    }

    /**
     * Guess what this function does
     */
    void displayAllStreets() {
        System.out.println("All streets:");
        for(Street street : streets){
            System.out.println(street.getName() + " From: " + street.getFirst().getName() + " To: " + street.getSecond().getName());
        }
        System.out.println();
    }




}
