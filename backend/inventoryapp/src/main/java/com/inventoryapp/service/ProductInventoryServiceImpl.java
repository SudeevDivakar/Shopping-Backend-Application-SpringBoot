package com.inventoryapp.service;

import com.inventoryapp.dto.InventoryDto;
import com.inventoryapp.entities.Product;
import com.inventoryapp.exceptions.IncorrectValueException;
import com.inventoryapp.exceptions.ProductNotFoundException;
import com.inventoryapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService{

    private final ProductRepository productRepository;

    @Autowired
    public ProductInventoryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Helper Function
    public Product getById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found in database"));
    }

    @Override
    public InventoryDto updateStock(String id, Integer quantity, Integer inc) {
        Product product = getById(id);

        // No negative quantity allowed
        if (quantity < 0) {
            throw new IncorrectValueException("Updating quantity cannot be negative");
        }

        else if (inc < 0) {
            // Check if user is trying to deduct too many products
            if (quantity > product.getStock()) {
                throw new IncorrectValueException("Quantity to reduce cannot be greater than existing stock");
            }
            //Reducing the quantity from stock
            quantity = quantity * -1;
        }

        product.setStock(quantity + product.getStock());
        productRepository.save(product);

        InventoryDto inventoryUpdatedDto = new InventoryDto();
        inventoryUpdatedDto.setStatus(true);
        inventoryUpdatedDto.setMessage("Inventory Updated Successfully");
        inventoryUpdatedDto.setStock(product.getStock());

        return inventoryUpdatedDto;
    }

    @Override
    public InventoryDto getStock(String id) {
        Product product = getById(id);

        InventoryDto inventoryFoundDto = new InventoryDto();
        inventoryFoundDto.setStatus(true);
        inventoryFoundDto.setMessage("Product Found with Stock - " + product.getStock());
        inventoryFoundDto.setStock(product.getStock());

        return inventoryFoundDto;
    }
}
