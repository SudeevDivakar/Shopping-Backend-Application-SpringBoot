package com.orderapp.service;

import com.orderapp.dto.OrderDto;

import java.util.List;

public interface OrderService {
    public List<OrderDto> getAllOrders();
    public List<OrderDto> getUserOrders(Long userId);
    public OrderDto getOrder(Long orderId);
    public OrderDto placeOrder(OrderDto orderDto);
    public OrderDto cancelOrder(Long orderId);
    public OrderDto deliverOrder(Long orderId);
}
