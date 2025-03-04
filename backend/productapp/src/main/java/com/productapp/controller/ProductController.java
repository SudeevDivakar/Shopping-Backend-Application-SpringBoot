package com.productapp.controller;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import com.productapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductDto getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PostMapping("/products")
    public ProductDto addProduct(@RequestBody @Valid ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    @PutMapping("/products/{id}")
    public ProductDto updateProduct(@PathVariable String id, @RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/products/{id}")
    public DeletedDto deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }
}
