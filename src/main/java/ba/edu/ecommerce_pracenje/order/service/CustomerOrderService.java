
package ba.edu.ecommerce_pracenje.order.service;

import ba.edu.ecommerce_pracenje.common.exception.ResourceNotFoundException;
import ba.edu.ecommerce_pracenje.customer.entity.Customer;
import ba.edu.ecommerce_pracenje.customer.repository.CustomerRepository;
import ba.edu.ecommerce_pracenje.order.dto.*;
import ba.edu.ecommerce_pracenje.order.entity.CustomerOrder;
import ba.edu.ecommerce_pracenje.order.entity.OrderItem;
import ba.edu.ecommerce_pracenje.order.repository.CustomerOrderRepository;
import ba.edu.ecommerce_pracenje.order.repository.OrderItemRepository;
import ba.edu.ecommerce_pracenje.product.entity.Product;
import ba.edu.ecommerce_pracenje.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerOrderService {

    private final CustomerOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CustomerOrderService(
            CustomerOrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        CustomerOrder order = new CustomerOrder();

        order.setCustomer(customer);
        order.setStatus("CREATED");

        order.setShippingName(request.getShippingName());
        order.setShippingStreet(request.getShippingStreet());
        order.setShippingCity(request.getShippingCity());
        order.setShippingPostalCode(request.getShippingPostalCode());
        order.setShippingCountry(request.getShippingCountry());

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();
        Set<Long> usedProductIds = new HashSet<>();

        for (OrderItemRequest itemRequest : request.getItems()) {

            Long productId = itemRequest.getProductId();

            if (!usedProductIds.add(productId)) {
                throw new IllegalArgumentException(
                        "A product cannot appear twice in the same order: " + productId);
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found: " + productId));

            if (!Boolean.TRUE.equals(product.getIsActive())) {
                throw new IllegalArgumentException(
                        "Product is not active: " + product.getName());
            }

            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity()));

            totalAmount = totalAmount.add(subtotal);

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(unitPrice);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);

        CustomerOrder savedOrder = orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);

        orderRepository.flush();

        entityManager.refresh(savedOrder);

        return mapToResponse(savedOrder, orderItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> mapToResponse(
                        order,
                        orderItemRepository.findByOrder_Id(order.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrder_Id(id);

        return mapToResponse(order, items);
    }

    private OrderResponse mapToResponse(
            CustomerOrder order,
            List<OrderItem> orderItems) {

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> {

                    BigDecimal subtotal = item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new OrderItemResponse(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            subtotal
                    );
                })
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingName(),
                order.getShippingStreet(),
                order.getShippingCity(),
                order.getShippingPostalCode(),
                order.getShippingCountry(),
                order.getCreatedAt(),
                itemResponses
        );
    }
}
