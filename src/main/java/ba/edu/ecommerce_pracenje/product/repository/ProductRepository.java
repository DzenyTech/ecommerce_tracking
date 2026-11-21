
package ba.edu.ecommerce_pracenje.product.repository;

import ba.edu.ecommerce_pracenje.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIsActiveTrue();

    List<Product> findByNameContainingIgnoreCase(String name);
}
