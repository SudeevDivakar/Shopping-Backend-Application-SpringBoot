package com.reviews.util;

import com.reviews.dto.ReviewDto;
import com.reviews.entities.Review;

public class ReviewConverter {
    public static ReviewDto reviewToReviewDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getReview(),
                review.getProductId(),
                review.getRating(),
                review.getUserEmail()
        );
    }

    public static Review reviewDtoToReview(ReviewDto reviewDto) {
        return new Review(
                reviewDto.getReview(),
                reviewDto.getProductId(),
                reviewDto.getRating(),
                reviewDto.getUserEmail()
        );
    }
}
