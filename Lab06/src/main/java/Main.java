import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws Exception {

        Connection conn = DatabaseConnection.getInstance().getConnection();
        GenreDAO genreDAO = new GenreDAO(conn);

        genreDAO.create("Action");
        genreDAO.create("Drama");
        genreDAO.create("Comedy");

        System.out.println("Created Action with id: ");
        System.out.println("Created Drama  with id: ");
        System.out.println("Created Comedy with id: ");

        String name = genreDAO.findById(1);
        System.out.println("findById(" + 1 + ") -> " + name);

        int id = genreDAO.findByName("Drama");
        System.out.println("findByName(\"Drama\") -> " + id);

        System.out.println("findById(999)        -> " + genreDAO.findById(999));
        System.out.println("findByName(\"Horror\") -> " + genreDAO.findByName("Horror"));
    }
}
