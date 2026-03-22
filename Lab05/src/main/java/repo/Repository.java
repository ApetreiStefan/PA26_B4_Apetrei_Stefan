package repo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class Repository {
    List<Resource> resources;

    public Repository(List<Resource> resources) {
        this.resources = resources;
    }

    public Repository() {}

    public Resource getResource(String id) {
        for (Resource resource : resources) {
            if(resource.getId().equals(id)) {
                return resource;
            }
        }
        return null;
    }
}
