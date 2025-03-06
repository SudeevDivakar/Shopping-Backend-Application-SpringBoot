package com.orderapp.dto;

import com.orderapp.util.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Item ID cannot be empty")
    private String itemId;

    @NotNull(message = "Quantity ordered is required")
    @Min(value = 0, message = "Quantity ordered cannot be negative")
    private Integer quantityOrdered;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    private Double amount;

    private OrderStatus status;
}

