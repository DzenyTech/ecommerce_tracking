
package ba.edu.ecommerce_pracenje.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long customerId;
    private String status;
    private BigDecimal totalAmount;

    private String shippingName;
    private String shippingStreet;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

    public OrderResponse(
            Long id,
            Long customerId,
            String status,
            BigDecimal totalAmount,
            String shippingName,
            String shippingStreet,
            String shippingCity,
            String shippingPostalCode,
            String shippingCountry,
            LocalDateTime createdAt,
            List<OrderItemResponse> items) {

        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingName = shippingName;
        this.shippingStreet = shippingStreet;
        this.shippingCity = shippingCity;
        this.shippingPostalCode = shippingPostalCode;
        this.shippingCountry = shippingCountry;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getShippingName() {
        return shippingName;
    }

    public String getShippingStreet() {
        return shippingStreet;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}
