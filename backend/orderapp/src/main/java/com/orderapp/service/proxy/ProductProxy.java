package com.orderapp.service.proxy;

import com.orderapp.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productapp", url = "localhost:8080")
public interface ProductProxy {
    @GetMapping("api/v1/products/{id}")
    @CircuitBreaker(name = "products", fallbackMethod = "fallbackMethod")
    public ProductDto retrieveProductDetails(@PathVariable String id);

    default ProductDto fallbackMethod(String id, Throwable ex) {
        throw new RuntimeException("Product Service Down");
    }
}
