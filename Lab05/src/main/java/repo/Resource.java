package repo;

import java.util.EnumSet;
import java.util.Set;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }
}