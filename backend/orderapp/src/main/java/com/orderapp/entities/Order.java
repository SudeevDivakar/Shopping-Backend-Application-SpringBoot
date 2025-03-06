package com.orderapp.entities;

import com.orderapp.util.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String itemId;
    private Integer quantityOrdered;
    private Double amount;
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Long userId, String itemId, Integer quantityOrdered, Double amount, String address, OrderStatus status) {
        this.userId = userId;
        this.itemId = itemId;
        this.quantityOrdered = quantityOrdered;
        this.amount = amount;
        this.address = address;
        this.status = status;
    }
}
