import Locations.*;
import Roads.*;
import Matematica.Point;
import BestRoute.Map;

public class Main{

    public static void main(String[] args) {
        Map bestRoute = new Map();
        Location tempLocation1;
        Location tempLocation2;
        Road tempRoad;

        tempLocation1 = new Airport("Aeroport Iasi", 20,43);
        bestRoute.addLocation(tempLocation1);
        bestRoute.addLocation(tempLocation1);

        tempLocation2 = new City("Iasi", 19, 40);
        bestRoute.addLocation(tempLocation2);

        tempRoad = new Road(RoadType.NATIONAL,10, tempLocation1, tempLocation2);
        bestRoute.addRoad(tempRoad);

        if(bestRoute.validate() == false){
            System.out.println("Problema este modelata incorect!");
            return;
        }

        if(bestRoute.canReach(tempLocation1, tempLocation2)){
            System.out.println("Am ajuns!");
        }
        else
        {
            System.out.println("Nu am putut ajunge");
        }

        bestRoute.resetVisits();

    }
}