import Locations.*;
import Roads.*;
import BestRoute.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Map largeMap = new Map();

        int nrOrase = 20;
        largeMap.generateRandomInstance(nrOrase, 3);

        if(!largeMap.validate()){
            System.out.println("Problema este modelata incorect!");
            return;
        }

        Location start = largeMap.getLocation("City 0");
        Location end = largeMap.getLocation("City " + (nrOrase - 1));

        System.out.println("Incepem testul de performanta");

        long startTime = System.currentTimeMillis();
        Solution sol1 = new Solution(start);
        largeMap.shortestRoute(start, end, sol1, null);
        long endTime = System.currentTimeMillis();

        System.out.println("Shortest Route gasita in: " + (endTime - startTime) + " ms");
        sol1.printSolution();

        largeMap.resetVisits();

        startTime = System.currentTimeMillis();
        Solution sol2 = new Solution(start);
        largeMap.fastestRoute(start, end, sol2);
        endTime = System.currentTimeMillis();

        System.out.println("\nFastest Route gasita in: " + (endTime - startTime) + " ms");
        sol2.printSolution();
    }
}