package com.orderapp.util;

import com.orderapp.dto.OrderDto;
import com.orderapp.entities.Order;

public class OrderConverter {
    public static Order orderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setQuantityOrdered(orderDto.getQuantityOrdered());
        order.setAmount(orderDto.getAmount());
        order.setUserId(orderDto.getUserId());
        order.setStatus(orderDto.getStatus());
        order.setItemId(orderDto.getItemId());
        order.setAddress(orderDto.getAddress());
        return order;
    }

    public static OrderDto orderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setItemId(order.getItemId());
        orderDto.setUserId(order.getUserId());
        orderDto.setAddress(order.getAddress());
        orderDto.setStatus(order.getStatus());
        orderDto.setQuantityOrdered(order.getQuantityOrdered());
        orderDto.setAmount(order.getAmount());
        return orderDto;
    }
}
