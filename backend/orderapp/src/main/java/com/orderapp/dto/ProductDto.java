package com.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Integer stock;
    private Double price;
    private String imageUrl;
    private Float rating;

    public ProductDto(String name, String description, Integer stock, Double price, String imageUrl, Float rating) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }
}