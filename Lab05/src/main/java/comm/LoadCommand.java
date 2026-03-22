package comm;

import repo.Repository;
import repo.RepositoryActions;

public class LoadCommand implements Command{

    Repository repository;
    String path;

    public LoadCommand(Repository repository, String path) {
        this.repository = repository;
        this.path = path;
    }

    public void run(){
        repository.setResources(RepositoryActions.generateResourceListFromResFile(path));
        System.out.println("Loading " + path + " from repository");
    }
}
