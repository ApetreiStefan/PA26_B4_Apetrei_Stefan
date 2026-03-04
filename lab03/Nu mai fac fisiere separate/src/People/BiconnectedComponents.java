package People;

import java.util.*;

/**
 * O varianta modificata a clasei People.ArticulationPoints
 * Modifica algoritmul Hopcroft-Tarjan pentru a pastra detalii in plus
 * Pe scurt, cand extragem un punct de articulatie, ce ramane in urma e o componenta biconexa
 * Fiecare componenta e o lista
 * Mai multe astfel de liste formeaza o lista de liste :p
 */
public class BiconnectedComponents {
    private int timer = 0;
    private Map<Integer, Integer> discoveryTime = new HashMap<>();
    private Map<Integer, Integer> lowLink = new HashMap<>();
    private Stack<Edge> stack = new Stack<>();
    private ArrayList<ArrayList<Profile>> components = new ArrayList<>();
    private Map<Integer, Profile> allProfiles;

    private static class Edge {
        Profile u, v;
        Edge(Profile u, Profile v) { this.u = u; this.v = v; }
    }

    public BiconnectedComponents(Map<Integer, Profile> allProfiles) {
        this.allProfiles = allProfiles;
    }

    /**
     * Foloseste Hopcroft-Tarjan modificat pentru a gasi componente biconexe
     * @return o lista de liste :p
     */
    public ArrayList<ArrayList<Profile>> findComponents() {
        for (Integer id : allProfiles.keySet()) discoveryTime.put(id, -1);

        for (Integer id : allProfiles.keySet()) {
            if (discoveryTime.get(id) == -1) {
                dfs(allProfiles.get(id), null);

                if (!stack.isEmpty()) {
                    addComponent();
                }
            }
        }
        return components;
    }

    private void dfs(Profile u, Profile p) {
        discoveryTime.put(u.getID(), lowLink.put(u.getID(), timer++));

        for (Profile v : u.getConnections().values()) {
            if (p != null && v.getID() == p.getID()) continue;

            if (discoveryTime.get(v.getID()) < discoveryTime.get(u.getID())) {
                if (discoveryTime.get(v.getID()) == -1) {
                    stack.push(new Edge(u, v));
                    dfs(v, u);
                    lowLink.put(u.getID(), Math.min(lowLink.get(u.getID()), lowLink.get(v.getID())));

                    if (lowLink.get(v.getID()) >= discoveryTime.get(u.getID())) {
                        extractComponent(u, v);
                    }
                } else {
                    stack.push(new Edge(u, v));
                    lowLink.put(u.getID(), Math.min(lowLink.get(u.getID()), discoveryTime.get(v.getID())));
                }
            }
        }
    }

    private void extractComponent(Profile u, Profile v) {
        Set<Profile> currentComp = new HashSet<>();
        while (true) {
            Edge e = stack.pop();
            currentComp.add(e.u);
            currentComp.add(e.v);
            if (e.u.getID() == u.getID() && e.v.getID() == v.getID()) break;
        }
        components.add(new ArrayList<>(currentComp));
    }

    private void addComponent() {
        Set<Profile> currentComp = new HashSet<>();
        while (!stack.isEmpty()) {
            Edge e = stack.pop();
            currentComp.add(e.u);
            currentComp.add(e.v);
        }
        components.add(new ArrayList<>(currentComp));
    }
}