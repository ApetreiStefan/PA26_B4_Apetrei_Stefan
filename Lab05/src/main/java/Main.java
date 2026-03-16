import repo.Repository;
import repo.RepositoryActions;

public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository(RepositoryActions.generateResourceListFromResFile("C:\\Users\\stefa\\Documents\\GitHub\\PA26_B4_Apetrei_Stefan\\Lab05\\src\\main\\java\\Resources.res"));
        RepositoryActions.printAll(repository);
        System.out.println();

       // RepositoryActions.openFile(repository.getResource("jvm25"));
    }
}
