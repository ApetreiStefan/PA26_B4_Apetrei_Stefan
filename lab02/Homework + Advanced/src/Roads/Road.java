package Roads;

import Locations.Location;
import Matematica.Point;
import static java.lang.Math.sqrt;

public class Road {

    private final int lengthKM;
    private final RoadType type;
    private final int speedLimmitKMH;
    private final Location location1, location2;
//---------------------------------------------
    public Road(RoadType type, int length, Location location1, Location location2){

        this.type = type;
        this.lengthKM = length;
        this.location1 = location1;
        this.location2 = location2;

        if(sqrt(
                (location1.getCoordinates().getX() -
                location2.getCoordinates().getX()) *
                (location1.getCoordinates().getX() -
                location2.getCoordinates().getX())
                                                   +
                (location1.getCoordinates().getY() -
                location2.getCoordinates().getY()) *
                (location1.getCoordinates().getY() -
                location2.getCoordinates().getY())
                ) >
                length
        ){
            throw new RuntimeException("lungime imposibila");
        }

        switch(type){
            case HIGHWAY:
            {
                this.speedLimmitKMH = 2000; // nu se observa diferenta altfel
                break;
            }
            case EUROPEAN:
            {
                this.speedLimmitKMH = 1000;
                break;
            }
            case NATIONAL:
            {
                this.speedLimmitKMH = 50;
                break;
            }
            default:
            {
                this.speedLimmitKMH = 0;
                break;
            }
        }
    }
//---------------------------------------------

    public int getLengthKM() {
        return lengthKM;
    }

    public RoadType getType() {
        return type;
    }

    public int getSpeedLimmitKMH() {
        return speedLimmitKMH;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public String toString(){
        return "\"" + type + "\"(" + lengthKM + " km) intre " + location1.toString() + " si " + location2.toString();
    }
    @Override
    public boolean equals(Object other){
        switch(other){
            case Road road:{
                if(
                        this.type == road.getType() &&
                        this.lengthKM == road.getLengthKM() &&
                        this.location1 == road.getLocation1() &&
                        this.location2 == road.getLocation2()
                ) return true;
                return false;
            }
            default: return false;
        }
    }

}

