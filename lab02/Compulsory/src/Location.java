public class Location {
    private String name;
    private String type;
    private Point coordinates;
//---------------------------------------------------
    public Location(String name){
        this.name = name;
    }

    public Location(String name, int x, int y){
        this.name = name;
        this.coordinates = new Point(x,y);
    }
//---------------------------------------------------

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){
        return String.format("\"" + name + "\"" + coordinates.toString());
    }
}
