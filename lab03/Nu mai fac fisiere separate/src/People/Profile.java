package People;

public interface Profile {
public <T extends Profile> void addRelationship(T other);
public int getID();
public String getName();
public int getImportance();
}
