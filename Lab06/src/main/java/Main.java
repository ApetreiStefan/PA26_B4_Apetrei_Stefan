import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws Exception {

        Connection conn = DatabaseConnection.getInstance().getConnection();
        GenreDAO genreDAO = new GenreDAO(conn);

        int actionId = genreDAO.create("Action");
        int dramaId  = genreDAO.create("Drama");
        int comedyId = genreDAO.create("Comedy");

        System.out.println("Created Action with id: " + actionId);
        System.out.println("Created Drama  with id: " + dramaId);
        System.out.println("Created Comedy with id: " + comedyId);

        String name = genreDAO.findById(actionId);
        System.out.println("findById(" + actionId + ") -> " + name);

        int id = genreDAO.findByName("Drama");
        System.out.println("findByName(\"Drama\") -> " + id);

        System.out.println("findById(999)        -> " + genreDAO.findById(999));
        System.out.println("findByName(\"Horror\") -> " + genreDAO.findByName("Horror"));
    }
}
