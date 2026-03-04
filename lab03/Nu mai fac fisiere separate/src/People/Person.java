package People;
import java.lang.Comparable;
import java.util.Map;
import java.util.TreeMap;

public class Person implements Comparable<Person>, Profile {
    private static int currentID = 0;
    private String name;
    private int ID;
    private int birthDay;
    private int birthMonth;
    private int birthYear;

    private TreeMap<Integer, Profile> connections;
//---------------------------------------------
    public Person(){}

    public Person(String name){
        this.name = name;
        this.ID = GlobalID.maxID;
        GlobalID.maxID = GlobalID.maxID + 1;
        connections = new TreeMap<>();
    }

//-------------------------------------------------------------
    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.getName());
    }

    @Override
    public <T extends Profile> void addRelationship(T other) {
        connections.put(other.getID(),other);
    }


    @Override
    public int getImportance() {
        return connections.size();
    }

    @Override
    public String toString() {
        return "Profilul (ID:" + ID + " Nume:" + name + ")";
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
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

