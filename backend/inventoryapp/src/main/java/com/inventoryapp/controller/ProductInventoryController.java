package com.inventoryapp.controller;

import com.inventoryapp.dto.InventoryFoundDto;
import com.inventoryapp.dto.InventoryUpdatedDto;
import com.inventoryapp.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ProductInventoryController {

    private final ProductInventoryService productInventoryService;

    @Autowired
    public ProductInventoryController(ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<InventoryFoundDto> getQuantity(@PathVariable String id) {
        return ResponseEntity.ok(productInventoryService.getQuantity(id));
    }

    @PatchMapping("/inventory/{id}/{quantity}")
    public ResponseEntity<InventoryUpdatedDto> addToInventory(@PathVariable String id, @PathVariable Integer quantity, @RequestParam(defaultValue = "1") Integer inc) {
        return ResponseEntity.ok(productInventoryService.updateInventory(id, quantity, inc));
    }
}
