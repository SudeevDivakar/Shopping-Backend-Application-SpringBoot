package com.productapp.service;

import com.productapp.dto.DeletedDto;
import com.productapp.dto.ProductDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    public Page<ProductDto> getAllProducts(int page, int size);
    public ProductDto getProductById(String id);
    public ProductDto addProduct(ProductDto productDto);
    public ProductDto updateProduct(String id, ProductDto productDto);
    public DeletedDto deleteProduct(String id);
}
