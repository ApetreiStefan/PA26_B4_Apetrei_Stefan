package comm;

import repo.Repository;
import repo.RepositoryActions;

public class ListCommand implements Command {

    Repository repository;

    public ListCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        RepositoryActions.printAll(repository);
        System.out.println("Listing");
    }
}
