
package ba.edu.ecommerce_pracenje.address.dto;

import java.time.LocalDateTime;

public class AddressResponse {

    private Long id;
    private Long customerId;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private LocalDateTime createdAt;

    public AddressResponse() {
    }

    public AddressResponse(
            Long id,
            Long customerId,
            String street,
            String city,
            String postalCode,
            String country,
            LocalDateTime createdAt) {

        this.id = id;
        this.customerId = customerId;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
