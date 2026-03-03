package People;

import java.lang.Comparable;

public class Company implements Comparable<Company>, Profile{
    private static int currentID = 0;
    private String name;
    private int ID;

    public Company(String name) {
        this.name = name;
        this.ID = currentID;
        currentID = currentID + 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public int compareTo(Company o) {
        return name.compareTo(o.getName());
    }

    @Override
    public void addRelationship(Person other) {

    }

    @Override
    public void addRelationship(Company other) {

    }

    @Override
    public int getImportance() {
        return 0;
    }
}
