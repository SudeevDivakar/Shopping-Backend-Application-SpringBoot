package com.reviews.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String review;
    private String productId;
    private Integer rating;
    private String userEmail;

    public Review(String review, String productId, Integer rating, String userEmail) {
        this.review = review;
        this.productId = productId;
        this.rating = rating;
        this.userEmail = userEmail;
    }
}
