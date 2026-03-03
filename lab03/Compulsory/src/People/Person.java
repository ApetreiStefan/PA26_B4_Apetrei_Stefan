package People;
import java.lang.Comparable;

public class Person implements Comparable<Person>, Profile {
    private static int currentID = 0;
    private String name;
    private int ID;
//---------------------------------------------
    public Person(){}

    public Person(String name){
        this.ID = currentID;
        this.name = name;
        currentID = currentID + 1;
    }

//-------------------------------------------------------------
    @Override
    public int compareTo(Person o) {
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
}

