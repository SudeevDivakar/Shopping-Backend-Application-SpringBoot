package com.productapp.util;

import com.productapp.dto.ProductDto;
import com.productapp.entities.Product;

public class ProductConverter {
    public static Product productDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrl());
        product.setRating(productDto.getRating());
        return product;
    }

    public static ProductDto productToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setRating(product.getRating());
        return productDto;
    }
}
