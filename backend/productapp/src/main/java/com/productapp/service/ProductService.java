package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;

import java.util.List;

public interface ProductService {
    public List<ProductDto> getAllProducts();
    public ProductDto getProductById(String id);
    public ProductDto addProduct(ProductDto productDto);
    public ProductDto updateProduct(String id, ProductDto productDto);
    public DeletedDto deleteProduct(String id);
}
