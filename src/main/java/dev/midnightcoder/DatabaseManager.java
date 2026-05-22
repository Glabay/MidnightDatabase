package dev.midnightcoder;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project MidnightDatabase
 * @social Discord: Glabay
 * @since 2026-05-22
 */
public interface DatabaseManager {
    void saveProduct(Product product);
    void removeProduct(Product product);

    List<Product> getAllProducts();
}
