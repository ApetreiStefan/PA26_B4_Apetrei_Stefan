import Locations.*;
import Roads.*;
import Matematica.Point;
import BestRoute.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Map bestRoute = new Map();
        Location l1, l2, l3;

        l1 = new City("Iasi", 0, 0);
        l2 = new City("AutostradaIntreIasiSiVaslui", 50, 40);
        l3 = new City("Vaslui", 100, 0);

        bestRoute.addLocation(l1);
        bestRoute.addLocation(l2);
        bestRoute.addLocation(l3);

        bestRoute.addRoad(new Road(RoadType.NATIONAL, 100, l1, l3));
        bestRoute.addRoad(new Road(RoadType.HIGHWAY, 65, l1, l2));
        bestRoute.addRoad(new Road(RoadType.HIGHWAY, 65, l2, l3));

        if(bestRoute.validate() == false){
            System.out.println("Problema este modelata incorect!");
            return;
        }

        if(bestRoute.canReach(l1, l3)){
            System.out.println("Am ajuns!");
        }
        else {
            System.out.println("Nu am putut ajunge");
        }
        bestRoute.resetVisits();

        System.out.println();
        System.out.println("Cea mai scurta ruta:");
        Solution sol1 = new Solution(l1);
        bestRoute.shortestRoute(l1, l3, sol1, null);
        sol1.printSolution();
        bestRoute.resetVisits();

        System.out.println();
        System.out.println("Cea mai rapida ruta:");
        Solution sol2 = new Solution(l1);
        bestRoute.fastestRoute(l1, l3, sol2);
        sol2.printSolution();
        bestRoute.resetVisits();
    }
}