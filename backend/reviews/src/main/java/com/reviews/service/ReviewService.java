package com.reviews.service;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {
    public List<ReviewDto> getAllReviews();
    public Page<ReviewDto> getProductReviews(String productId, int page, int size);
    public Page<ReviewDto> getUserReviews(String userEmail, int page, int size);
    public ReviewDto getReview(String reviewId);
    public ReviewDto addReview(ReviewDto reviewDto);
    public DeletedDto deleteReview(String reviewId);
}
