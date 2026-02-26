package BestRoute;
import Roads.*;
import Matematica.*;
import Locations.*;

import java.util.ArrayList;

public class Solution {
    private ArrayList<Road> roads = new ArrayList<>();
    private Location firstLocation;
//-----------------------------------------------

    public Solution(Location firstLocation){
        this.firstLocation = firstLocation;
    }

//-----------------------------------------------
    public ArrayList<Road> getRoads() { return roads; }
    public void setRoads(ArrayList<Road> roads) { this.roads = roads; }

    public void printSolution() {
        System.out.println(firstLocation);
        Location current = firstLocation;
        for (Road r : roads) {
            if (r.getLocation1().equals(current)) {
                current = r.getLocation2();
            } else {
                current = r.getLocation1();
            }
            System.out.println(current);
        }
    }
}

