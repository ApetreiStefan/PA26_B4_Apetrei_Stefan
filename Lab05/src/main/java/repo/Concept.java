package repo;

public enum Concept {
    GRAPH_THEORY("Graph Theory"),
    NEURAL_NETWORKS("Neural Networks"),
    ALGORITHM_DESIGN("Algorithm Design Techniques"),
    OOP("Object-Oriented Programming"),
    SORTING_SEARCHING("Sorting & Searching"),
    COMPLEXITY_THEORY("Complexity Theory"),
    MACHINE_LEARNING("Machine Learning"),
    DATA_STRUCTURES("Data Structures"),
    DISTRIBUTED_SYSTEMS("Distributed Systems"),
    CRYPTOGRAPHY("Cryptography");

    private final String displayName;

    Concept(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}