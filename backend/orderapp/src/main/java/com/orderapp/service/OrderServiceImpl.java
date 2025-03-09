package com.orderapp.service;

import com.common.NotificationDto;
import com.orderapp.dto.InventoryDto;
import com.orderapp.dto.OrderDto;
import com.orderapp.dto.ProductDto;
import com.orderapp.entities.Order;
import com.orderapp.exceptions.InsufficientStockException;
import com.orderapp.exceptions.InventoryUpdateException;
import com.orderapp.exceptions.OrderNotFoundException;
import com.orderapp.exceptions.OrderStatusUpdateException;
import com.orderapp.repositories.OrderRepository;
import com.orderapp.service.proxy.InventoryProxy;
import com.orderapp.service.proxy.ProductProxy;
import com.orderapp.util.OrderConverter;
import com.orderapp.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;
    private final ProductProxy productProxy;
    private final InventoryProxy inventoryProxy;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, NotificationDto> kafkaTemplate, ProductProxy productProxy, InventoryProxy inventoryProxy) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.productProxy = productProxy;
        this.inventoryProxy = inventoryProxy;
    }

    // Helper Functions to Send API requests

    public InventoryDto updateStock(String id, Integer quantity, int inc) {
        return inventoryProxy.updateStock(id, quantity, inc);
    }

    public ProductDto retrieveProductDetails(String id) {
        return productProxy.retrieveProductDetails(id);
    }

    public InventoryDto retrieveInventoryStock(String id) {
        return inventoryProxy.retrieveInventoryStock(id);
    }

    // End of Helper Functions to Send API requests

    @Override
    public List<OrderDto> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream().map(OrderConverter::orderToOrderDto).toList();
    }

    @Override
    public List<OrderDto> getUserOrders(String userEmail) {
        log.info("Fetching orders for user: {}", userEmail);
        return orderRepository.findByUserEmail(userEmail).stream().map(OrderConverter::orderToOrderDto).toList();
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        return OrderConverter.orderToOrderDto(orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with ID: {} not found", orderId);
                    return new OrderNotFoundException("Order not found in database");
                }));
    }

    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
        log.info("Placing order for user: {}", orderDto.getUserEmail());
        ProductDto product = retrieveProductDetails(orderDto.getItemId());
        Double price = product.getPrice();
        Double totalAmount = orderDto.getQuantityOrdered() * price;

        int currentStock = retrieveInventoryStock(orderDto.getItemId()).getStock();
        if (orderDto.getQuantityOrdered() > currentStock) {
            log.error("Insufficient stock for product: {}", orderDto.getItemId());
            throw new InsufficientStockException("Insufficient Stock in Inventory");
        }

        Order order = new Order(orderDto.getUserEmail(), orderDto.getItemId(), orderDto.getQuantityOrdered(), totalAmount, orderDto.getAddress(), OrderStatus.PLACED);
        InventoryDto inventoryUpdateResponse = updateStock(orderDto.getItemId(), orderDto.getQuantityOrdered(), -1);

        if (inventoryUpdateResponse.getStock() != currentStock - orderDto.getQuantityOrdered()) {
            log.error("Inventory update failed for product: {}", orderDto.getItemId());
            throw new InventoryUpdateException("Inventory Update Failed");
        }

        kafkaTemplate.send("order-placed-topic", new NotificationDto(orderDto.getUserEmail(), product.getName(), orderDto.getQuantityOrdered(), totalAmount, orderDto.getAddress()));
        log.info("Order placed successfully for user: {}", orderDto.getUserEmail());
        return OrderConverter.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        log.info("Cancelling order with ID: {}", orderId);
        Order order = OrderConverter.orderDtoToOrder(getOrder(orderId));
        ProductDto product = retrieveProductDetails(order.getItemId());

        if (order.getStatus() == OrderStatus.DELIVERED) {
            log.error("Order ID: {} is already delivered and cannot be cancelled", orderId);
            throw new OrderStatusUpdateException("Order is delivered, cannot be cancelled");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            log.warn("Order ID: {} is already cancelled", orderId);
            throw new OrderStatusUpdateException("Order is already cancelled");
        }

        int currentStock = retrieveInventoryStock(order.getItemId()).getStock();
        InventoryDto inventoryUpdateResponse = updateStock(order.getItemId(), order.getQuantityOrdered(), 1);

        if (inventoryUpdateResponse.getStock() != currentStock + order.getQuantityOrdered()) {
            log.error("Inventory update failed for product: {} during order cancellation", order.getItemId());
            throw new InventoryUpdateException("Inventory Update Failed");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        kafkaTemplate.send("order-cancelled-topic", new NotificationDto(order.getUserEmail(), product.getName(), order.getQuantityOrdered(), order.getAmount(), order.getAddress()));
        log.info("Order ID: {} successfully cancelled", orderId);
        return OrderConverter.orderToOrderDto(order);
    }

    @Override
    public OrderDto deliverOrder(Long orderId) {
        log.info("Delivering order with ID: {}", orderId);
        Order order = OrderConverter.orderDtoToOrder(getOrder(orderId));
        ProductDto product = retrieveProductDetails(order.getItemId());

        if (order.getStatus() == OrderStatus.CANCELLED) {
            log.error("Order ID: {} is cancelled and cannot be delivered", orderId);
            throw new OrderStatusUpdateException("Order is cancelled, cannot be delivered");
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            log.warn("Order ID: {} is already delivered", orderId);
            throw new OrderStatusUpdateException("Order is already delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        kafkaTemplate.send("order-delivered-topic", new NotificationDto(order.getUserEmail(), product.getName(), order.getQuantityOrdered(), order.getAmount(), order.getAddress()));
        log.info("Order ID: {} successfully delivered", orderId);
        return OrderConverter.orderToOrderDto(order);
    }
}
