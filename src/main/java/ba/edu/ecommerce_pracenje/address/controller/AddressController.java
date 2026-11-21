
package ba.edu.ecommerce_pracenje.address.controller;

import ba.edu.ecommerce_pracenje.address.dto.AddressResponse;
import ba.edu.ecommerce_pracenje.address.dto.CreateAddressRequest;
import ba.edu.ecommerce_pracenje.address.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(
            @PathVariable Long id) {

        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByCustomerId(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                addressService.getAddressesByCustomerId(customerId)
        );
    }

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {

        AddressResponse response = addressService.createAddress(request);

        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {

        addressService.deleteAddress(id);

        return ResponseEntity.noContent().build();
    }
}
