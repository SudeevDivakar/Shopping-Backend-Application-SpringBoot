package com.inventoryapp.service;

import com.inventoryapp.dto.InventoryDto;

public interface ProductInventoryService {
    public InventoryDto updateInventory(String id, Integer quantity, Integer inc);
    public InventoryDto getQuantity(String id);
}
