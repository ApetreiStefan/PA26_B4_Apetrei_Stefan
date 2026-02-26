package BestRoute;

import java.util.ArrayList;
import Locations.*;
import Roads.*;

public class Map { // Clasa BestRoute.Map modeleaza o instanta a problemei
    ArrayList<Location> locations = new ArrayList<>();
    ArrayList<Road> roads = new ArrayList<>();
//-------------------------------------------------
    public Map(){
        
    }
//-------------------------------------------------
    public void addLocation(Location l){
        for(Location i : locations){
            if(i.equals(l)){
                System.out.println("Eroare: locatia " + l.getName() + " a fost deja adaugata!");
                return;
            }
        }
        locations.add(l);
    }

    public void addRoad(Road r){
        roads.add(r);
    }

    public Location getLocation(String name){
        for(Location l : locations){
            if(l.getName().equals(name)){
                return l;
            }
        }
        return null;
    }

    public boolean validate(){ // metoda validate verifica daca instanta problemei a fost completata corect
        if(locations.isEmpty()){ // verificam daca au fost introduce locatii
            return false;
        }
        else if(roads.isEmpty()){ // verificam daca au fost adaugate drumuri
            return false;
        }
        return true; // alte verificari sunt facute in constructorii claselor respective
    }
/**
 * Returns true if location2 can be reached from location1 using the roads
 * This method uses Dijkstra's algorithm and is a recursive function
 * After this method you must use "resetVisits()", otherwise locations will remain visited
 * @param l1 The start location of the algorithm
 * @param l2 The end location of the algorithm / the one we are trying to reach
 * @return true if it succeeds and false if it doesn't
 */
    public boolean canReach(Location l1, Location l2){ // canReach este o functie recursiva care aplica djkstra de la l1 pana la l2
        boolean answer = false;
        if(l1.isVisited()) return false; // conditie de oprire
        l1.setVisited(true);
        for(Road r : roads){
            if(r.getLocation1() == l1 && r.getLocation2() == l2){ // daca il gasim pe l2 in drumul nostru, returnam true
                return true;
            }
            else if(r.getLocation1() == l2 && r.getLocation2() == l1){
                return true;
            }
            else if(r.getLocation1() == l1){ // pentru toti vecinii lui l1 reapelam functia
                answer = answer || canReach(r.getLocation2(), l2);
            }
        }
        return answer;
    }

    /**
     * Resets the "visited" field to false for all locations
     * Is required to be called after "canReach"
     */
    public void resetVisits(){ // resetVisits reseteaza toate variabilele "visited" din locatii
        for(Location l : locations){
            l.setVisited(false);
        }
    }
}