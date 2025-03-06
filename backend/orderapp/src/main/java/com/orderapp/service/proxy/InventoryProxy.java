package com.orderapp.service.proxy;

import com.orderapp.dto.InventoryDto;
import com.orderapp.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventoryapp", url = "localhost:8082")
public interface InventoryProxy {
    @GetMapping("api/v1/inventory/{id}")
    public InventoryDto retrieveInventoryStock(@PathVariable String id);

    @PutMapping("api/v1/inventory/{id}/{quantity}")
    public InventoryDto updateStock(@PathVariable String id, @PathVariable Integer quantity, @RequestParam(name = "inc") Integer inc);
}


