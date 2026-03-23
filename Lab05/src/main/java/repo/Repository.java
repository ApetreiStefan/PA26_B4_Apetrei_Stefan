package repo;

import java.util.List;

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

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
