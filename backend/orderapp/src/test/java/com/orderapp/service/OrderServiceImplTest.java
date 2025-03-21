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
import com.orderapp.util.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    KafkaTemplate<String, NotificationDto> kafkaTemplate;
    @Mock
    ProductProxy productProxy;
    @Mock
    InventoryProxy inventoryProxy;
    @InjectMocks
    OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateStock() {
        when(inventoryProxy.updateStock(anyString(), anyInt(), anyInt())).thenReturn(new InventoryDto(true, "message", 0));

        InventoryDto result = orderServiceImpl.updateStock("id", 0, 0);
        Assertions.assertEquals(new InventoryDto(true, "message", 0), result);
    }

    @Test
    void testRetrieveProductDetails() {
        when(productProxy.retrieveProductDetails(anyString())).thenReturn(new ProductDto("id", "name", "description", 0, 0.0, "imageUrl", 1.1f));

        ProductDto result = orderServiceImpl.retrieveProductDetails("id");
        Assertions.assertEquals(new ProductDto("id", "name", "description", 0, 0.0, "imageUrl", 1.1f), result);
    }

    @Test
    void testRetrieveInventoryStock() {
        when(inventoryProxy.retrieveInventoryStock(anyString())).thenReturn(new InventoryDto(true, "message", 0));

        InventoryDto result = orderServiceImpl.retrieveInventoryStock("id");
        Assertions.assertEquals(new InventoryDto(true, "message", 0), result);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(1L, "userEmail", "itemId", 1, 10.0, "address", OrderStatus.PLACED)));

        List<OrderDto> result = orderServiceImpl.getAllOrders();
        Assertions.assertEquals(List.of(new OrderDto(1L, "userEmail", "itemId", 1, "address", 10.0, OrderStatus.PLACED)), result);
    }

    @Test
    void testGetUserOrders() {
        when(orderRepository.findByUserEmail(anyString())).thenReturn(List.of(new Order(1L, "userEmail", "itemId", 1, 10.0, "address", OrderStatus.PLACED)));

        List<OrderDto> result = orderServiceImpl.getUserOrders("userEmail");
        Assertions.assertEquals(List.of(new OrderDto(1L, "userEmail", "itemId", 1, "address", 10.0, OrderStatus.PLACED)), result);
    }

    @Test
    void testGetOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order(1L, "userEmail", "itemId", 1, 10.0, "address", OrderStatus.PLACED)));

        OrderDto result = orderServiceImpl.getOrder(1L);
        Assertions.assertEquals(new OrderDto(1L, "userEmail", "itemId", 1, "address", 10.0, OrderStatus.PLACED), result);
    }

    @Test
    void testGetOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderServiceImpl.getOrder(1L));
    }

    @Test
    void testPlaceOrder() {
        when(productProxy.retrieveProductDetails(anyString())).thenReturn(new ProductDto("id", "name", "description", 10, 10.0, "imageUrl", 1.1f));
        when(inventoryProxy.retrieveInventoryStock(anyString())).thenReturn(new InventoryDto(true, "message", 10));
        when(inventoryProxy.updateStock(anyString(), anyInt(), anyInt())).thenReturn(new InventoryDto(true, "message", 5));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.PLACED));

        OrderDto result = orderServiceImpl.placeOrder(new OrderDto(1L, "userEmail", "itemId", 5, "address", 50.0, OrderStatus.PLACED));
        Assertions.assertEquals(new OrderDto(1L, "userEmail", "itemId", 5, "address", 50.0, OrderStatus.PLACED), result);
    }

    @Test
    void testPlaceOrderInsufficientStock() {
        when(productProxy.retrieveProductDetails(anyString())).thenReturn(new ProductDto("id", "name", "description", 10, 10.0, "imageUrl", 1.1f));
        when(inventoryProxy.retrieveInventoryStock(anyString())).thenReturn(new InventoryDto(true, "message", 5));

        Assertions.assertThrows(InsufficientStockException.class, () -> orderServiceImpl.placeOrder(new OrderDto(1L, "userEmail", "itemId", 10, "address", 100.0, OrderStatus.PLACED)));
    }

    @Test
    void testPlaceOrderInventoryUpdateFailed() {
        when(productProxy.retrieveProductDetails(anyString())).thenReturn(new ProductDto("id", "name", "description", 10, 10.0, "imageUrl", 1.1f));
        when(inventoryProxy.retrieveInventoryStock(anyString())).thenReturn(new InventoryDto(true, "message", 10));
        when(inventoryProxy.updateStock(anyString(), anyInt(), anyInt())).thenReturn(new InventoryDto(true, "message", 10));

        Assertions.assertThrows(InventoryUpdateException.class, () -> orderServiceImpl.placeOrder(new OrderDto(1L, "userEmail", "itemId", 5, "address", 50.0, OrderStatus.PLACED)));
    }

    @Test
    void testCancelOrder() {
        // Mock the order to be cancelled
        Order order = new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.PLACED);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        ProductDto product = new ProductDto("itemId", "Product Name", "Description", 10, 10.0, "imageUrl", 4.5f);
        when(productProxy.retrieveProductDetails(anyString())).thenReturn(product);

        when(inventoryProxy.retrieveInventoryStock(anyString())).thenReturn(new InventoryDto(true, "message", 5));
        when(inventoryProxy.updateStock(anyString(), anyInt(), anyInt())).thenReturn(new InventoryDto(true, "message", 10));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto result = orderServiceImpl.cancelOrder(1L);

        Assertions.assertEquals(new OrderDto(1L, "userEmail", "itemId", 5, "address", 50.0, OrderStatus.CANCELLED), result);
    }

    @Test
    void testCancelOrderAlreadyDelivered() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.DELIVERED)));

        Assertions.assertThrows(OrderStatusUpdateException.class, () -> orderServiceImpl.cancelOrder(1L));
    }

    @Test
    void testCancelOrderAlreadyCancelled() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.CANCELLED)));

        Assertions.assertThrows(OrderStatusUpdateException.class, () -> orderServiceImpl.cancelOrder(1L));
    }

    @Test
    void testCancelOrderInventoryUpdateFailed() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.PLACED)));
        when(inventoryProxy.retrieveInventoryStock(anyString())).thenReturn(new InventoryDto(true, "message", 5));
        when(inventoryProxy.updateStock(anyString(), anyInt(), anyInt())).thenReturn(new InventoryDto(true, "message", 5));

        Assertions.assertThrows(InventoryUpdateException.class, () -> orderServiceImpl.cancelOrder(1L));
    }

    @Test
    void testDeliverOrder() {
        // Mock the order to be delivered
        Order order = new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.PLACED);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        // Mock the product details
        ProductDto product = new ProductDto("itemId", "Product Name", "Description", 10, 10.0, "imageUrl", 4.5f);
        when(productProxy.retrieveProductDetails(anyString())).thenReturn(product);

        // Mock the order save
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Call the method
        OrderDto result = orderServiceImpl.deliverOrder(1L);

        // Verify the result
        Assertions.assertEquals(new OrderDto(1L, "userEmail", "itemId", 5, "address", 50.0, OrderStatus.DELIVERED), result);
    }

    @Test
    void testDeliverOrderAlreadyCancelled() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.CANCELLED)));

        Assertions.assertThrows(OrderStatusUpdateException.class, () -> orderServiceImpl.deliverOrder(1L));
    }

    @Test
    void testDeliverOrderAlreadyDelivered() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order(1L, "userEmail", "itemId", 5, 50.0, "address", OrderStatus.DELIVERED)));

        Assertions.assertThrows(OrderStatusUpdateException.class, () -> orderServiceImpl.deliverOrder(1L));
    }
}