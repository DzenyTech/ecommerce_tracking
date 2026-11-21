
package ba.edu.ecommerce_pracenje.address.repository;

import ba.edu.ecommerce_pracenje.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCustomer_Id(Long customerId);
}
