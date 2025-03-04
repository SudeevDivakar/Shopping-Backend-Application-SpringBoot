package com.inventoryapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryFoundDto {
    private boolean found;
    private String message;
    private Integer stock;
}
