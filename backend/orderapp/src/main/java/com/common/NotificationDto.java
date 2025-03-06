package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String to;
    private String productName;
    private Integer quantity;
    private Double amount;
    private String address;
}
