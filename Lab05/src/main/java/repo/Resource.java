package repo;

import lombok.Getter;
import lombok.Setter;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
public class Resource {
    private String id;
    private String title;
    private String location;
    private String year;
    private String author;
    private Set<Concept> concepts; // NEW: set of ACM concepts

    public Resource(String id, String title, String location,
                    String year, String author, Set<Concept> concepts) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.author = author;
        this.concepts = concepts != null ? concepts : EnumSet.noneOf(Concept.class);
    }

    // Backward-compatible constructor (no concepts)
    public Resource(String id, String title, String location, String year, String author) {
        this(id, title, location, year, author, EnumSet.noneOf(Concept.class));
    }

    @Override
    public String toString() {
        return "id: " + id
                + ", title: " + title
                + ", location: " + location
                + ", year: " + year
                + ", author: " + author
                + ", concepts: " + concepts;
    }
}