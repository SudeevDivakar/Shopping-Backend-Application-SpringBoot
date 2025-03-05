package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import com.productapp.entities.Product;
import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repositories.ProductRepository;
import com.productapp.util.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(ProductConverter::productToProductDto).toList();
    }

    public ProductDto getProductById(String id) {
        return ProductConverter.productToProductDto(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found in database")));
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product productToBeAdded = ProductConverter.productDtoToProduct(productDto);
        return ProductConverter.productToProductDto(productRepository.save(productToBeAdded));
    }

    public ProductDto updateProduct(String id, ProductDto productDto) {
        // Check if product exists
        Product productToUpdate = ProductConverter.productDtoToProduct(getProductById(id));

        // Update product
        Product newProduct = ProductConverter.productDtoToProduct(productDto);
        newProduct.setId(id);

        return ProductConverter.productToProductDto(productRepository.save(newProduct));
    }

    public DeletedDto deleteProduct(String id) {
        // Check if product exists
        Product productToUpdate = ProductConverter.productDtoToProduct(getProductById(id));

        // Delete product
        productRepository.deleteById(id);

        DeletedDto deletedDto = new DeletedDto();
        deletedDto.setDeleted(true);
        deletedDto.setMessage("Product Successfully Deleted");
        return deletedDto;
    }
}
