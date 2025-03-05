package com.productapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    @NotBlank(message = "Image URL cannot be empty")
    private String imageUrl;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 5, message = "Rating cannot be more than 5")
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
