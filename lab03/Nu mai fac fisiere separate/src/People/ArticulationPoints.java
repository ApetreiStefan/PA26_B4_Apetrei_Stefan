package People;

import java.util.*;

// E bine ca fiecare clasa sa aiba un scop unic, deci am facut alta pentru punctele de articulatie

/**
 * O clasa care ajuta la gasirea punctelor de articulatie intr-o retea
 */
public class ArticulationPoints {
    private int timer;
    private Map<Integer, Integer> discoveryTime;
    private Map<Integer, Integer> lowLink;
    private Set<Integer> articulationIDs;
    private Map<Integer, Profile> allProfiles;

    /**
     * @param allProfiles A map containing all the profiles in a network
     */
    public ArticulationPoints(Map<Integer, Profile> allProfiles) {
        this.allProfiles = allProfiles;
        this.discoveryTime = new HashMap<>();
        this.lowLink = new HashMap<>();
        this.articulationIDs = new HashSet<>();
        this.timer = 0;
    }

    /**
     * Aceasta functie foloseste algoritmul Hopcroft-Tarjan pentru a determina punctele de articulatie din retea
     * @return Returneaza punctele de articulatie din retea
     */
    public ArrayList<Profile> find() {
        for (Integer id : allProfiles.keySet()) {
            discoveryTime.put(id, -1);
        }

        for (Integer id : allProfiles.keySet()) {
            if (discoveryTime.get(id) == -1) {
                dfs(allProfiles.get(id), null);
            }
        }

        ArrayList<Profile> result = new ArrayList<>();
        for (Integer id : articulationIDs) {
            result.add(allProfiles.get(id));
        }
        return result;
    }

    private void dfs(Profile u, Profile p) {
        int uID = u.getID();
        discoveryTime.put(uID, timer);
        lowLink.put(uID, timer);
        timer++;
        int children = 0;

        for (Profile v : u.getConnections().values()) {
            int vID = v.getID();

            if (p != null && vID == p.getID()) continue;

            if (discoveryTime.get(vID) != -1) {
                lowLink.put(uID, Math.min(lowLink.get(uID), discoveryTime.get(vID)));
            } else {
                children++;
                dfs(v, u);
                lowLink.put(uID, Math.min(lowLink.get(uID), lowLink.get(vID)));

                if (p != null && lowLink.get(vID) >= discoveryTime.get(uID)) {
                    articulationIDs.add(uID);
                }
            }
        }

        if (p == null && children > 1) {
            articulationIDs.add(uID);
        }
    }
}