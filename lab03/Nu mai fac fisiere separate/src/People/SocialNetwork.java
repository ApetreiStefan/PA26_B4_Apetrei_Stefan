package People;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SocialNetwork {

    TreeMap<Integer, Profile> sortedProfiles; // this uses Importance as a key
    TreeMap<Integer, Profile> profiles; // this uses ID as a key
//-------------------------------------------
    public SocialNetwork(){
        this.profiles = new TreeMap<>();
        GlobalID.maxID = 0;
    }
//-------------------------------------------
    public int getImportance(Profile p){
        return p.getImportance();
    }

    /**
     * This function adds a relationship between two profiles using their IDs
     * The data regarding this is stored in the object, in a TreeMap
     * Waring: This function does not check if the IDs are valid
     * @param ID1 This is the ID of the first Profile
     * @param ID2 This is the ID of the second Profile
     */
    public void addRelationship(int ID1,int ID2){
        profiles.get(ID1).addRelationship(profiles.get(ID2));
        profiles.get(ID2).addRelationship(profiles.get(ID1));
    }

//    /**
//     * This function adds a relationship between two profiles using their names
//     * The data regarding this is stored in the object, in a TreeMap
//     * Waring: This function does not check if the names are valid
//     * @param name1 This is the name of the first Profile
//     * @param name2 This is the name of the second Profile
//     */
//    public void addRelationship(String name1, String name2){
//        for(Map.Entry<Integer, Profile> it : profiles.entrySet()){
//            if(it.getValue().getName().equals(name1)){
//                it.getValue().addRelationship();
//            }
//        }
//    }
    // M-am razgandit :/

    /**
     * Aceasta functie printeaza toate profilurile
     * Se foloseste de toString(), supraincarcata in Person si Company
     * Folosim un TreeMap setat la reverse order pentru a obtine sortarea dupa importanta
     * Importanta este reprezentata in fiecare clasa de marimea listei de relatii + 1
     */
    //Acum observ ca am scris unele comentarii in romana si unele in engleza :c
    public void print(){
        sortedProfiles = new TreeMap<>(Collections.reverseOrder());
        for(Map.Entry<Integer, Profile> it : profiles.entrySet()){
            sortedProfiles.put(it.getValue().getImportance(), it.getValue());
        }
        for(Map.Entry<Integer, Profile> it : sortedProfiles.entrySet()) {
            System.out.println(it.getValue().toString());
        }
    }

    public TreeMap<Integer, Profile> getProfiles(){return profiles;}

    public void addProfile(Profile p){
        profiles.put(p.getID(), p);
    }

    private void dfs(Profile current, Profile parent){

    }
}
