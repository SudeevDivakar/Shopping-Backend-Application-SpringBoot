package com.inventoryapp.service;

import com.inventoryapp.dto.InventoryDto;
import com.inventoryapp.entities.Product;
import com.inventoryapp.exceptions.IncorrectValueException;
import com.inventoryapp.exceptions.ProductNotFoundException;
import com.inventoryapp.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductInventoryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductInventoryServiceImpl productInventoryServiceImpl;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product("id", "Test Product", "Test Description", 10, 100.0, "imageUrl", 4.5f);
    }

    @Test
    void testGetById_ProductExists() {
        when(productRepository.findById("id")).thenReturn(Optional.of(sampleProduct));

        Product result = productInventoryServiceImpl.getById("id");
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void testGetById_ProductNotFound() {
        when(productRepository.findById("invalid_id")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productInventoryServiceImpl.getById("invalid_id"));
    }

    @Test
    void testUpdateStock_IncrementStock() {
        when(productRepository.findById("id")).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        InventoryDto result = productInventoryServiceImpl.updateStock("id", 5, 1);

        assertTrue(result.isStatus());
        assertEquals("Inventory Updated Successfully", result.getMessage());
        assertEquals(15, result.getStock());
    }

    @Test
    void testUpdateStock_DecrementStock() {
        when(productRepository.findById("id")).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        InventoryDto result = productInventoryServiceImpl.updateStock("id", 5, -1);

        assertTrue(result.isStatus());
        assertEquals("Inventory Updated Successfully", result.getMessage());
        assertEquals(5, result.getStock());
    }

    @Test
    void testUpdateStock_DecrementStockExceedingAvailable() {
        when(productRepository.findById("id")).thenReturn(Optional.of(sampleProduct));

        assertThrows(IncorrectValueException.class, () -> productInventoryServiceImpl.updateStock("id", 15, -1));
    }

    @Test
    void testUpdateStock_NegativeQuantity() {
        when(productRepository.findById("id")).thenReturn(Optional.of(sampleProduct));

        assertThrows(IncorrectValueException.class, () -> productInventoryServiceImpl.updateStock("id", -5, 1));
    }

    @Test
    void testGetStock_ProductExists() {
        when(productRepository.findById("id")).thenReturn(Optional.of(sampleProduct));

        InventoryDto result = productInventoryServiceImpl.getStock("id");

        assertTrue(result.isStatus());
        assertEquals("Product Found with Stock - 10", result.getMessage());
        assertEquals(10, result.getStock());
    }

    @Test
    void testGetStock_ProductNotFound() {
        when(productRepository.findById("invalid_id")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productInventoryServiceImpl.getStock("invalid_id"));
    }
}
