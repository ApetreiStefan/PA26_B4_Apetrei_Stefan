package db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton that manages a C3P0 connection pool.
 * On first initialization, Flyway runs all pending migrations automatically.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    private static DatabaseConnection instance;
    private final ComboPooledDataSource pool;

    private DatabaseConnection() throws PropertyVetoException {
        // 1. Run Flyway migrations before opening the pool
        Flyway.configure()
                .dataSource(URL, USERNAME, PASSWORD)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)   // safe to run even if DB already has tables
                .load()
                .migrate();

        // 2. Set up the connection pool
        pool = new ComboPooledDataSource();
        pool.setDriverClass("org.postgresql.Driver");
        pool.setJdbcUrl(URL);
        pool.setUser(USERNAME);
        pool.setPassword(PASSWORD);
        pool.setMinPoolSize(2);
        pool.setMaxPoolSize(10);
        pool.setInitialPoolSize(2);
        pool.setCheckoutTimeout(5000);
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            try {
                instance = new DatabaseConnection();
            } catch (PropertyVetoException e) {
                throw new SQLException("Failed to initialize connection pool.", e);
            }
        }
        return instance;
    }

    /** Borrows a connection from the pool. MUST be closed after use (try-with-resources). */
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    /** Shuts down the pool. Call once on application exit. */
    public void shutdown() {
        pool.close();
    }
}