
package ba.edu.ecommerce_pracenje.order.repository;

import ba.edu.ecommerce_pracenje.order.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository
        extends JpaRepository<CustomerOrder, Long> {
}
