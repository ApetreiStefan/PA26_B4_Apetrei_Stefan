import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:movies.db";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        createTables();
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS genres (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS actors (id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT NOT NULL, last_name TEXT NOT NULL)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS movies (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, release_date TEXT, duration INTEGER, score REAL, genre_id INTEGER REFERENCES genres(id))");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS movie_actor (movie_id INTEGER REFERENCES movies(id), actor_id INTEGER REFERENCES actors(id), PRIMARY KEY (movie_id, actor_id))");
        st.close();
    }
}
