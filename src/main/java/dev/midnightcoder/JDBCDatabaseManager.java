package dev.midnightcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project MidnightDatabase
 * @social Discord: Glabay
 * @since 2026-05-21
 */
public class JDBCDatabaseManager implements DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(JDBCDatabaseManager.class);

    private static final String creationQuery = """
        CREATE TABLE IF NOT EXISTS products (
            name TEXT NOT NULL,
            price DOUBLE PRECISION NOT NULL,
            quantity INTEGER NOT NULL
        )
    """;

    private static final String query = """
        INSERT INTO products (name, price, quantity)
               VALUES (?, ?, ?)
    """;

    private static final String queryAll = """
        SELECT * FROM products
    """;

    public JDBCDatabaseManager() {
        // Initialize database tables if they don't exist
        try (var conn = getConnection()) {
            conn.prepareStatement(creationQuery).executeUpdate();
        }
        catch (SQLException e) {
            logger.error("Failed to initialize database tables", e);
        }
    }

    @Override
    public void saveProduct(Product product) {
        try (var conn = getConnection()) {
            var st = conn.prepareStatement(query);
                st.setString(1, product.name());
                st.setDouble(2, product.price());
                st.setInt(3, product.quantity());
                st.executeUpdate();
        }
        catch (SQLException e) {
            logger.error("Failed to save product to database", e);
        }
    }

    @Override
    public void removeProduct(Product product) {
        try (var conn = getConnection()) {
            var st = conn.prepareStatement(query);
                st.setString(1, product.name());
                st.executeUpdate();
        }
        catch (SQLException e) {
            logger.error("Failed to remove product from database", e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try (var conn = getConnection()) {
            var st = conn.prepareStatement(queryAll);
            var rs = st.executeQuery();
            var products = new ArrayList<Product>();
            while (rs.next()) {
                products.add(new Product(
                    rs.getString(1),
                    rs.getDouble(2),
                    rs.getInt(3))
                );
            }
            return products;
        }
        catch (SQLException e) {
            logger.error("Failed to retrieve products from database", e);
            return Collections.emptyList();
        }
    }

    private Connection getConnection() {
        try {
            var url = "jdbc:sqlite:.data/midnight_database.db";
            var username = "Glabay";
            var password = "MidnightCoder";
            return DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

}
