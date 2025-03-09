package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import com.productapp.entities.Product;
import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repositories.ProductRepository;
import com.productapp.repositories.ReviewRepository;
import com.productapp.util.ProductConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ProductDto> getAllProducts() {
        log.info("Fetching all products from the database");
        List<ProductDto> products = productRepository.findAll().stream().map(ProductConverter::productToProductDto).toList();
        log.debug("Retrieved {} products", products.size());
        return products;
    }

    public ProductDto getProductById(String id) {
        log.info("Fetching product with ID: {}", id);
        ProductDto productDto = ProductConverter.productToProductDto(
                productRepository.findById(id).orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new ProductNotFoundException("Product not found in database");
                })
        );
        log.debug("Product retrieved: {}", productDto);
        return productDto;
    }

    public ProductDto addProduct(ProductDto productDto) {
        log.info("Adding new product: {}", productDto);
        Product productToBeAdded = ProductConverter.productDtoToProduct(productDto);
        productToBeAdded.setRating(0.0f);
        ProductDto savedProduct = ProductConverter.productToProductDto(productRepository.save(productToBeAdded));
        log.info("Product added successfully: {}", savedProduct);
        return savedProduct;
    }

    public ProductDto updateProduct(String id, ProductDto productDto) {
        log.info("Updating product with ID: {}", id);
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found", id);
            return new ProductNotFoundException("Product not found in database");
        });

        productToUpdate.setName(productDto.getName());
        productToUpdate.setPrice(productDto.getPrice());
        productToUpdate.setImageUrl(productDto.getImageUrl());
        productToUpdate.setStock(productDto.getStock());
        productToUpdate.setDescription(productDto.getDescription());

        ProductDto updatedProduct = ProductConverter.productToProductDto(productRepository.save(productToUpdate));
        log.info("Product updated successfully: {}", updatedProduct);
        return updatedProduct;
    }

    public DeletedDto deleteProduct(String id) {
        log.info("Deleting product with ID: {}", id);
        Product productToUpdate = ProductConverter.productDtoToProduct(getProductById(id));

        log.info("Deleting reviews for product ID: {}", id);
        reviewRepository.deleteByProductId(id);

        log.info("Deleting product from database");
        productRepository.deleteById(id);

        log.info("Product deleted successfully with ID: {}", id);
        return new DeletedDto(true, "Product Successfully Deleted");
    }
}
