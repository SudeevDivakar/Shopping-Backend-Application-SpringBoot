package com.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String id;

    @NotBlank(message = "Review cannot be empty")
    private String review;

    @NotBlank(message = "Product ID cannot be empty")
    private String productId;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    private String userEmail;

    public ReviewDto(String review, String productId, Integer rating, String userEmail) {
        this.review = review;
        this.productId = productId;
        this.rating = rating;
        this.userEmail = userEmail;
    }
}