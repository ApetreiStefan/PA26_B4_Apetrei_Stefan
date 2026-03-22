package comm;

import except.RepositoryException;
import repo.Repository;
import repo.RepositoryActions;

public class ViewCommand implements Command {

    Repository repository;
    String id;

    public ViewCommand(Repository repository, String id) {
        this.repository = repository;
        this.id = id;
    }

    public void run(){
        try{
            if(repository.getResource(id) == null){throw new RepositoryException("Resursa indisponibila");}
        }
        catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
        RepositoryActions.openFile(repository.getResource(id));
        System.out.println("viewing " + repository.getResource(id));

    }
}
