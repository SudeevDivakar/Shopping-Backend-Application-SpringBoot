package com.orderapp.controller;

import com.orderapp.dto.OrderDto;
import com.orderapp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getUserOrders(id));
    }

    @PostMapping("")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderDto));
    }

    @PutMapping("deliver/{id}")
    public ResponseEntity<OrderDto> deliverOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deliverOrder(id));
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

}
