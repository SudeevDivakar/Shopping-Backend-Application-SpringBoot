package com.inventoryapp.service;

import com.inventoryapp.dto.InventoryFoundDto;
import com.inventoryapp.dto.InventoryUpdatedDto;

public interface ProductInventoryService {
    public InventoryUpdatedDto updateInventory(String id, Integer quantity, Integer inc);
    public InventoryFoundDto getQuantity(String id);
}
