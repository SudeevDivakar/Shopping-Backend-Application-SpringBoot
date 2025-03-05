package com.inventoryapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private Integer stock;
    private Double price;
    private String imageUrl;
    private Float rating;

    public Product(String name, String description, Integer stock, Double price, String imageUrl, Float rating) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }
}
