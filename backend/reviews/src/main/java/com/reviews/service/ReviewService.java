package com.reviews.service;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    public List<ReviewDto> getAllReviews();
    public List<ReviewDto> getProductReviews(String productId);
    public List<ReviewDto> getUserReviews(String userEmail);
    public ReviewDto getReview(String reviewId);
    public ReviewDto addReview(ReviewDto reviewDto);
    public DeletedDto deleteReview(String reviewId);
}
