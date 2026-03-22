//import comm.*;
//import repo.Repository;
//import repo.RepositoryActions;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Queue;
//
//public class Main {
//    public static void main(String[] args) {
//        Repository repository = new Repository();
//        Queue<Command> commandList = new LinkedList<>();
//
//        commandList.add(new LoadCommand(repository, "C:\\Users\\stefa\\Documents\\GitHub\\PA26_B4_Apetrei_Stefan\\Lab05\\src\\main\\java\\Resources.json"));
//        commandList.add(new ListCommand(repository));
//        commandList.add(new ReportCommand(repository));
//        //commandList.add(new ViewCommand(repository, "jvm25"));
//
//        commandList.poll().run();
//        commandList.poll().run();
//        commandList.poll().run();
//        //commandList.poll().run();
//
//        System.out.println();
//    }
//}

import repo.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        // --- Build a sample repository with concept assignments ---
        List<Resource> resources = List.of(
                new Resource("1", "Introduction to Graph Theory", "/books/graph.pdf", "2018",
                        "Diestel", EnumSet.of(Concept.GRAPH_THEORY, Concept.ALGORITHM_DESIGN,
                        Concept.COMPLEXITY_THEORY)),
                new Resource("2", "Deep Learning", "/books/dl.pdf", "2016",
                        "Goodfellow", EnumSet.of(Concept.NEURAL_NETWORKS, Concept.MACHINE_LEARNING,
                        Concept.DATA_STRUCTURES)),
                new Resource("3", "Design Patterns", "/books/dp.pdf", "1994",
                        "GoF", EnumSet.of(Concept.OOP, Concept.ALGORITHM_DESIGN)),
                new Resource("4", "Algorithm Design", "/books/algo.pdf", "2005",
                        "Kleinberg", EnumSet.of(Concept.ALGORITHM_DESIGN, Concept.GRAPH_THEORY,
                        Concept.COMPLEXITY_THEORY, Concept.SORTING_SEARCHING)),
                new Resource("5", "Distributed Systems: Principles", "/books/dist.pdf", "2017",
                        "Tanenbaum", EnumSet.of(Concept.DISTRIBUTED_SYSTEMS, Concept.CRYPTOGRAPHY)),
                new Resource("6", "Introduction to Algorithms (CLRS)", "/books/clrs.pdf", "2009",
                        "Cormen", EnumSet.of(Concept.SORTING_SEARCHING, Concept.DATA_STRUCTURES,
                        Concept.GRAPH_THEORY, Concept.COMPLEXITY_THEORY)),
                new Resource("7", "Pattern Recognition and ML", "/books/prml.pdf", "2006",
                        "Bishop", EnumSet.of(Concept.MACHINE_LEARNING, Concept.NEURAL_NETWORKS)),
                new Resource("8", "Applied Cryptography", "/books/crypto.pdf", "2015",
                        "Ferguson", EnumSet.of(Concept.CRYPTOGRAPHY, Concept.DISTRIBUTED_SYSTEMS)),
                new Resource("9", "Object-Oriented Analysis and Design", "/books/ooad.pdf", "2004",
                        "Larman", EnumSet.of(Concept.OOP, Concept.ALGORITHM_DESIGN)),
                new Resource("10", "Artificial Intelligence: A Modern Approach", "/books/aima.pdf", "2020",
                        "Russell", EnumSet.of(Concept.MACHINE_LEARNING, Concept.NEURAL_NETWORKS,
                        Concept.GRAPH_THEORY))
        );

        Repository repo = new Repository(resources);
        Set<Concept> universe = EnumSet.allOf(Concept.class);

        System.out.println("=== Resource Repository ===");
        RepositoryActions.printAll(repo);

        System.out.println("\n=== Universe of Concepts ===");
        universe.forEach(c -> System.out.println("  - " + c));

        System.out.println("\n=== Greedy Approximation ===");
        List<Resource> greedy = SetCoverSolver.greedyCover(repo, universe);
        printCover(greedy);

        System.out.println("\n=== Exact Minimum Cover ===");
        List<Resource> exact = SetCoverSolver.exactMinimumCover(repo, universe);
        printCover(exact);

        System.out.println("\n");
        SetCoverSolver.runBenchmark();
    }

    static void printCover(List<Resource> cover) {
        if (cover == null) { System.out.println("  No feasible cover found."); return; }
        System.out.println("  Size: " + cover.size());
        for (Resource r : cover) {
            System.out.println("  [" + r.getId() + "] " + r.getTitle()
                    + " -> " + r.getConcepts());
        }
    }
}
