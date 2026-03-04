package Locations;

import Matematica.Point;

public sealed abstract class Location permits City, Airport, GasStation  {
    private String name;
    private Point coordinates;

    private boolean visited = false; // o variabila care ne ajuta la Djkstra in Map
//---------------------------------------------------
    public Location(String name){
        this.name = name;
    } // Constructor care ia doar nume

    public Location(String name, int x, int y){ // Constructor care ia nume si locatie
        this.name = name;
        this.coordinates = new Point(x,y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

//---------------------------------------------------

    public boolean equals(Object other){
//Verificam pentru fiecare tip de Location daca e egal cu cel original
        switch(other){
            case City city: {
                return this instanceof City &&
                        this.name.equals(city.getName()) &&
                        this.coordinates.equals(city.getCoordinates());
            }
            case Airport airport:{
                return this instanceof Airport &&
                        this.name.equals(airport.getName()) &&
                        this.coordinates.equals(airport.getCoordinates());
            }
            case GasStation gasStation:{
                return this instanceof GasStation &&
                        this.name.equals(gasStation.getName()) &&
                        this.coordinates.equals(gasStation.getCoordinates());
            }
            default: return false;
        }
    }
    public String toString(){
        return String.format("\"" + name + "\"" + coordinates.toString());
    }
}
