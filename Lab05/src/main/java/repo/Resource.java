package repo;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter

public class Resource {
    private String id;
    private String title;
    private String location;
    private String year;
    private String author;

    public Resource(String id, String title, String location, String year, String author) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.author = author;
    }

    @Override
    public String toString() {
        return "id: " + id +  ", title: " + title + ", location: " + location + ", year: " + year + ", author: " + author;
    }

}
