package com.inventoryapp.service;

import com.inventoryapp.dto.InventoryDto;

public interface ProductInventoryService {
    public InventoryDto updateStock(String id, Integer quantity, Integer inc);
    public InventoryDto getStock(String id);
}
