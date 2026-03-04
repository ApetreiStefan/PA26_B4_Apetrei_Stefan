package People;

import java.util.TreeMap;

public interface Profile {
public <T extends Profile> void addRelationship(T other);
public int getID();
public String getName();
public int getImportance();
public TreeMap<Integer, Profile> getConnections();
}
