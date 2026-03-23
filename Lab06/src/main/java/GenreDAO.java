import java.sql.*;
import java.util.UUID;

public class GenreDAO {

    private final Connection connection;

    public GenreDAO(Connection connection) {
        this.connection = connection;
    }

    public int create(String name) throws SQLException {
        String sql = "INSERT OR IGNORE INTO genres (name) VALUES (?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");
        }
    }

    public String findById(int id) throws SQLException {
        String sql = "SELECT name FROM genres WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("name") : null;
        }
    }

    public int findByName(String name) throws SQLException {
        String sql = "SELECT id FROM genres WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        }
    }
}
