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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return orderRepository.findAll().stream().map(OrderConverter::orderToOrderDto).toList();
    }

    @Override
    public List<OrderDto> getUserOrders(String userEmail) {
        return orderRepository.findByUserEmail(userEmail).stream().map(OrderConverter::orderToOrderDto).toList();
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return OrderConverter.orderToOrderDto(orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found in database")));
    }

    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
        // Get Price of item being ordered + Check if item exists in product database
        ProductDto product = retrieveProductDetails(orderDto.getItemId());
        Double price = product.getPrice();

        // Calculate Total Amount of Order
        Double totalAmount = orderDto.getQuantityOrdered() * price;

        // Check if sufficient stock is present
        int currentStock = retrieveInventoryStock(orderDto.getItemId()).getStock();
        if (orderDto.getQuantityOrdered() > currentStock) {
            throw new InsufficientStockException("Insufficient Stock in Inventory");
        }

        // Create Order
        Order order = new Order(orderDto.getUserEmail(),
                orderDto.getItemId(),
                orderDto.getQuantityOrdered(),
                totalAmount,
                orderDto.getAddress(),
                OrderStatus.PLACED);

        // Update Inventory Stock
        InventoryDto inventoryUpdateResponse = updateStock(orderDto.getItemId(), orderDto.getQuantityOrdered(), -1);
        if (inventoryUpdateResponse.getStock() != currentStock - orderDto.getQuantityOrdered()) {
            throw new InventoryUpdateException("Inventory Update Failed");
        }

        kafkaTemplate.send("order-placed-topic", new NotificationDto(orderDto.getUserEmail(),
                                                                            product.getName(),
                                                                            orderDto.getQuantityOrdered(),
                                                                            totalAmount,
                                                                            orderDto.getAddress()));

        return OrderConverter.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        // Check if order exists + get order
        Order order = OrderConverter.orderDtoToOrder(getOrder(orderId));

        ProductDto product = retrieveProductDetails(order.getItemId());

        // Get current stock
        int currentStock = retrieveInventoryStock(order.getItemId()).getStock();

        // Increase stock in database
        InventoryDto inventoryUpdateResponse = updateStock(order.getItemId(), order.getQuantityOrdered(), 1);

        if (inventoryUpdateResponse.getStock() != currentStock + order.getQuantityOrdered()) {
            throw new InventoryUpdateException("Inventory Update Failed");
        }

        // If order is already delivered, it cannot be cancelled
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderStatusUpdateException("Order is delivered, cannot be cancelled");
        }

        // If order is already cancelled, it cannot be cancelled again
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderStatusUpdateException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        kafkaTemplate.send("order-cancelled-topic", new NotificationDto(order.getUserEmail(),
                product.getName(),
                order.getQuantityOrdered(),
                order.getAmount(),
                order.getAddress()));

        return OrderConverter.orderToOrderDto(order);
    }

    @Override
    public OrderDto deliverOrder(Long orderId) {
        // Check if order exists
        Order order = OrderConverter.orderDtoToOrder(getOrder(orderId));

        // If order is already cancelled, it cannot be delivered
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderStatusUpdateException("Order is cancelled, cannot be delivered");
        }
        // If order is already delivered, it cannot be delivered again
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderStatusUpdateException("Order is already delivered");
        }

        // If order exists, change order status to 'DELIVERED'
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        ProductDto product = retrieveProductDetails(order.getItemId());

        kafkaTemplate.send("order-delivered-topic", new NotificationDto(order.getUserEmail(),
                product.getName(),
                order.getQuantityOrdered(),
                order.getAmount(),
                order.getAddress()));

        return OrderConverter.orderToOrderDto(order);
    }
}
