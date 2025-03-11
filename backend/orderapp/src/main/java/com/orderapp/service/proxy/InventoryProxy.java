package com.orderapp.service.proxy;

import com.orderapp.dto.InventoryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name = "inventoryapp", url = "localhost:8082")
@FeignClient(name = "inventoryapp", url = "http://inventory-service:8082")
public interface InventoryProxy {
    @GetMapping("api/v1/inventory/{id}")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public InventoryDto retrieveInventoryStock(@PathVariable String id);

    @PutMapping("api/v1/inventory/{id}/{quantity}")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public InventoryDto updateStock(@PathVariable String id, @PathVariable Integer quantity, @RequestParam(name = "inc") Integer inc);

    default InventoryDto fallbackMethod(String id, Throwable ex) {
        throw new RuntimeException("Inventory Service TemporarilyDown");
    }
}


