public class Street {
    private Intersection first;
    private Intersection second;
    private String name;
    private int length;

    public Street(Intersection first, Intersection second, String name, int length){
        this.first = first;
        this.second = second;
        this.name = name;
        this.length = length;
    }

    public Intersection getFirst() {
        return first;
    }

    public Intersection getSecond() {
        return second;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
