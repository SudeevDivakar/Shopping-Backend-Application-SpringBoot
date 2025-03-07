package com.productapp.entities;

import com.productapp.entities.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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
    private Float rating = 0.0f;
    private Long totalRating = 0L;
    private Long totalReviews = 0L;
    @DBRef
    private List<Review> reviews = new ArrayList<>();

    public Product(String name, String description, Integer stock, Double price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
