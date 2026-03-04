package People;

import java.lang.Comparable;
import java.util.TreeMap;

public class Company implements Comparable<Company>, Profile{
    private String name;
    private int ID;
    //Incep sa inteleg de ce numele campurilor nu se pune cu litera mare
    //Nu mi-am dat seama ce fac pana nu am avut nevoie de o clasa separata numita ID
    //Si dintr-odata nu ma pot referi la clasa din cauza campului :")
    //Si sa refactorizez codul... am sa zic pas

    private TreeMap<Integer, Profile> connections;

    /**
     * Numele este dat ca parametru, iar ID-ul este dedus cu ajutorul
     * clasei GlobalID, care tine cel mai mare ID folosit pana la momentul actual
     * @param name Numele companiei
     */
    public Company(String name) {
        this.name = name;
        this.ID = GlobalID.maxID;
        GlobalID.maxID = GlobalID.maxID + 1;
        connections = new TreeMap<>();
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

    public TreeMap<Integer, Profile> getConnections(){return connections;}

    @Override
    public int compareTo(Company o) {
        return name.compareTo(o.getName());
    }

    @Override
    public <T extends Profile> void addRelationship(T other) {
        connections.put(other.getID(), other);
    }


    //Un bug foarte interesant am avut aici
    //Aparent intr-un TreeMap nu poti adauga elemente cu cheia 0
    //Si cand incercam sa sortez harta in network, elementul asta nu avea legaturi
    //Si iteram prin harta si afisam si imi sarea elementul cu ID 2, adica unul de pe la mijlocul listei
    //Concluzie => size + 1
    @Override
    public int getImportance() {
        return connections.size() + 1;
    }

    @Override
    public String toString() {
        return "Profilul (ID:" + ID + " Nume:" + name + ")";
    }


}
