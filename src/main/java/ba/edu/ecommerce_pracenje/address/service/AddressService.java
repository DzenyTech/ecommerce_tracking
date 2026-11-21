
package ba.edu.ecommerce_pracenje.address.service;

import ba.edu.ecommerce_pracenje.address.dto.AddressResponse;
import ba.edu.ecommerce_pracenje.address.dto.CreateAddressRequest;
import ba.edu.ecommerce_pracenje.address.entity.Address;
import ba.edu.ecommerce_pracenje.address.repository.AddressRepository;
import ba.edu.ecommerce_pracenje.common.exception.ResourceNotFoundException;
import ba.edu.ecommerce_pracenje.customer.entity.Customer;
import ba.edu.ecommerce_pracenje.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressService(
            AddressRepository addressRepository,
            CustomerRepository customerRepository) {

        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AddressResponse getAddressById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + id
                        ));

        return mapToResponse(address);
    }

    public List<AddressResponse> getAddressesByCustomerId(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with id: " + customerId
            );
        }

        return addressRepository.findByCustomer_Id(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AddressResponse createAddress(CreateAddressRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: "
                                        + request.getCustomerId()
                        ));

        Address address = new Address();

        address.setCustomer(customer);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        Address savedAddress = addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    public void deleteAddress(Long id) {

        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Address not found with id: " + id
            );
        }

        addressRepository.deleteById(id);
    }

    private AddressResponse mapToResponse(Address address) {

        return new AddressResponse(
                address.getId(),
                address.getCustomer().getId(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry(),
                address.getCreatedAt()
        );
    }
}
