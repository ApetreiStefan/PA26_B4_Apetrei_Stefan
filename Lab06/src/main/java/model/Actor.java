package model;

public class Actor {
    private int id;
    private String firstName;
    private String lastName;

    public Actor(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId()           { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public void setId(int id)    { this.id = id; }

    public String getFullName()  { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return "model.Actor{id=" + id + ", name='" + getFullName() + "'}";
    }
}