package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import com.productapp.entities.Product;
import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repositories.ProductRepository;
import com.productapp.repositories.ReviewRepository;
import com.productapp.util.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return productRepository.findAll().stream().map(ProductConverter::productToProductDto).toList();
    }

    public ProductDto getProductById(String id) {
        return ProductConverter.productToProductDto(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found in database")));
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product productToBeAdded = ProductConverter.productDtoToProduct(productDto);
        productToBeAdded.setRating(0.0f);
        return ProductConverter.productToProductDto(productRepository.save(productToBeAdded));
    }

    public ProductDto updateProduct(String id, ProductDto productDto) {
        // Check if product exists
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found in database"));

        productToUpdate.setName(productDto.getName());
        productToUpdate.setPrice(productDto.getPrice());
        productToUpdate.setImageUrl(productDto.getImageUrl());
        productToUpdate.setStock(productDto.getStock());
        productToUpdate.setDescription(productDto.getDescription());

        return ProductConverter.productToProductDto(productRepository.save(productToUpdate));
    }

    public DeletedDto deleteProduct(String id) {
        // Check if product exists
        Product productToUpdate = ProductConverter.productDtoToProduct(getProductById(id));

        // Delete reviews
        reviewRepository.deleteByProductId(id);

        // Delete product
        productRepository.deleteById(id);

        return new DeletedDto(true, "Product Successfully Deleted");
    }
}
