package com.inventoryapp.service;

import com.inventoryapp.dto.InventoryDto;
import com.inventoryapp.entities.Product;
import com.inventoryapp.exceptions.IncorrectValueException;
import com.inventoryapp.exceptions.ProductNotFoundException;
import com.inventoryapp.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService{

    private final ProductRepository productRepository;

    @Autowired
    public ProductInventoryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Helper Function
    public Product getById(String id) {
        log.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new ProductNotFoundException("Product not found in database");
                });
    }

    @Override
    public InventoryDto updateStock(String id, Integer quantity, Integer inc) {
        log.info("Updating stock for product ID: {}, Quantity: {}, Increment flag: {}", id, quantity, inc);
        Product product = getById(id);

        if (quantity < 0) {
            log.error("Invalid quantity: {}. Updating quantity cannot be negative.", quantity);
            throw new IncorrectValueException("Updating quantity cannot be negative");
        }

        if (inc < 0) {
            if (quantity > product.getStock()) {
                log.error("Attempt to reduce {} units exceeds available stock of {}", quantity, product.getStock());
                throw new IncorrectValueException("Quantity to reduce cannot be greater than existing stock");
            }
            quantity = quantity * -1;
        }

        product.setStock(quantity + product.getStock());
        productRepository.save(product);
        log.info("Stock updated successfully for product ID: {}. New stock: {}", id, product.getStock());

        InventoryDto inventoryUpdatedDto = new InventoryDto();
        inventoryUpdatedDto.setStatus(true);
        inventoryUpdatedDto.setMessage("Inventory Updated Successfully");
        inventoryUpdatedDto.setStock(product.getStock());

        return inventoryUpdatedDto;
    }

    @Override
    public InventoryDto getStock(String id) {
        log.info("Fetching stock for product ID: {}", id);
        Product product = getById(id);

        InventoryDto inventoryFoundDto = new InventoryDto();
        inventoryFoundDto.setStatus(true);
        inventoryFoundDto.setMessage("Product Found with Stock - " + product.getStock());
        inventoryFoundDto.setStock(product.getStock());

        log.info("Stock for product ID {}: {}", id, product.getStock());
        return inventoryFoundDto;
    }
}
