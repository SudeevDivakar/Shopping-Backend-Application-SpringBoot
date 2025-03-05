package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import com.productapp.entities.Product;
import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repositories.ProductRepository;
import com.productapp.util.Converter;
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
        return productRepository.findAll().stream().map(Converter::productToProductDto).toList();
    }

    public ProductDto getProductById(String id) {
        return Converter.productToProductDto(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found in database")));
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product productToBeAdded = Converter.productDtoToProduct(productDto);
        return Converter.productToProductDto(productRepository.save(productToBeAdded));
    }

    public ProductDto updateProduct(String id, ProductDto productDto) {
        // Check if product exists
        Product productToUpdate = Converter.productDtoToProduct(getProductById(id));

        // Update product
        Product newProduct = Converter.productDtoToProduct(productDto);
        newProduct.setId(id);

        return Converter.productToProductDto(productRepository.save(newProduct));
    }

    public DeletedDto deleteProduct(String id) {
        // Check if product exists
        Product productToUpdate = Converter.productDtoToProduct(getProductById(id));

        // Delete product
        productRepository.deleteById(id);

        DeletedDto deletedDto = new DeletedDto();
        deletedDto.setDeleted(true);
        deletedDto.setMessage("Product Successfully Deleted");
        return deletedDto;
    }
}
