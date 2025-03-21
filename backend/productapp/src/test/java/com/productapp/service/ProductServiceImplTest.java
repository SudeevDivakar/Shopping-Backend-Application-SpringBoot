package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import com.productapp.entities.Product;
import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repositories.ProductRepository;
import com.productapp.repositories.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductServiceImpl productServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product = new Product("id", "name", "description", 10, 100.0, "imageUrl", 4.5f, 100L, 50L, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDto> result = productServiceImpl.getAllProducts(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("id", result.getContent().getFirst().getId());
        assertEquals(0, result.getNumber()); // Verify page number
        assertEquals(10, result.getSize()); // Verify page size
        assertEquals(1, result.getTotalElements()); // Verify total elements
    }

    @Test
    void testGetAllProducts_EmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDto> result = productServiceImpl.getAllProducts(0, 10);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements()); // Verify no elements
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById("id")).thenReturn(Optional.of(new Product("id", "name", "description", 10, 100.0, "imageUrl", 4.5f, 100L, 50L, null)));

        ProductDto result = productServiceImpl.getProductById("id");
        assertEquals("id", result.getId());
        assertEquals("name", result.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById("invalidId")).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productServiceImpl.getProductById("invalidId"));
    }

    @Test
    void testAddProduct() {
        ProductDto newProductDto = new ProductDto("id", "name", "description", 10, 100.0, "imageUrl", 4.5f);
        Product newProduct = new Product("id", "name", "description", 10, 100.0, "imageUrl", 0.0f, 0L, 0L, null);

        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        ProductDto result = productServiceImpl.addProduct(newProductDto);
        assertEquals("id", result.getId());
        assertEquals("name", result.getName());
    }

    @Test
    void testAddProduct_InvalidDetails() {
        ProductDto invalidProductDto = new ProductDto("", "", -1, -100.0, "");

        Assertions.assertThrows(Exception.class, () -> productServiceImpl.addProduct(invalidProductDto));
    }

    @Test
    void testUpdateProduct() {
        String productId = "id";
        Product existingProduct = new Product(productId, "oldName", "oldDesc", 10, 99.99, "oldImage", 4.5f, 100L, 50L, null);
        ProductDto updatedProductDto = new ProductDto(productId, "newName", "newDesc", 20, 199.99, "newImage", 5.0f);
        Product updatedProduct = new Product(productId, "newName", "newDesc", 20, 199.99, "newImage", 5.0f, 100L, 50L, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDto result = productServiceImpl.updateProduct(productId, updatedProductDto);

        assertEquals("newName", result.getName());
        assertEquals(199.99, result.getPrice());
    }

    @Test
    void testUpdateProduct_NotFound() {
        String productId = "invalidId";
        ProductDto updatedProductDto = new ProductDto(productId, "newName", "newDesc", 20, 199.99, "newImage", 5.0f);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productServiceImpl.updateProduct(productId, updatedProductDto));
    }

    @Test
    void testDeleteProduct() {
        String productId = "id";
        Product existingProduct = new Product(productId, "name", "description", 10, 100.0, "imageUrl", 4.5f, 100L, 50L, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(reviewRepository).deleteByProductId(productId);
        doNothing().when(productRepository).deleteById(productId);

        DeletedDto result = productServiceImpl.deleteProduct(productId);

        Assertions.assertTrue(result.isDeleted());
        assertEquals("Product Successfully Deleted", result.getMessage());
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById("invalidId")).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productServiceImpl.deleteProduct("invalidId"));
    }

    @Test
    void testDeleteProduct_ReviewDeletionFails() {
        String productId = "id";
        Product existingProduct = new Product(productId, "name", "description", 10, 100.0, "imageUrl", 4.5f, 100L, 50L, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doThrow(new RuntimeException("Review deletion failed")).when(reviewRepository).deleteByProductId(productId);

        Assertions.assertThrows(RuntimeException.class, () -> productServiceImpl.deleteProduct(productId));
    }
}
