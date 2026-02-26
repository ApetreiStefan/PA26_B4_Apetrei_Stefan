import static java.lang.Math.sqrt;

public class Road {
    private int lengthKM;
    private String type;
    private int speedLimmitKMH;
    private Location location1, location2;
//---------------------------------------------
    public Road(String type,int length, Location location1, Location location2){
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

        switch(type.toLowerCase()){
            case "highway":
            {
                this.speedLimmitKMH = 130;
                break;
            }
            case "european":
            {
                this.speedLimmitKMH = 100;
                break;
            }
            case "national":
            {
                this.speedLimmitKMH = 90;
                break;
            }
        }
    }
//---------------------------------------------
    public String toString(){
        return "\"" + type + "\"(" + lengthKM + " km) intre " + location1.toString() + " si " + location2.toString();
    }
}

