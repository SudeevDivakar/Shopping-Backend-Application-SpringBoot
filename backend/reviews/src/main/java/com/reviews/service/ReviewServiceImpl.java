package com.reviews.service;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;
import com.reviews.entities.Product;
import com.reviews.entities.Review;
import com.reviews.exceptions.ProductNotFoundException;
import com.reviews.exceptions.ReviewNotFoundException;
import com.reviews.repositories.ProductRepository;
import com.reviews.repositories.ReviewRepository;
import com.reviews.util.ReviewConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream().map(ReviewConverter::reviewToReviewDto).toList();
    }

    @Override
    public List<ReviewDto> getProductReviews(String productId) {
        return reviewRepository.findByProductId(productId).stream().map(ReviewConverter::reviewToReviewDto).toList();
    }

    @Override
    public List<ReviewDto> getUserReviews(String userEmail) {
        return reviewRepository.findByUserEmail(userEmail).stream().map(ReviewConverter::reviewToReviewDto).toList();
    }

    @Override
    public ReviewDto getReview(String reviewId) {
        return ReviewConverter.reviewToReviewDto(reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review not found in database")));
    }

    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        // Check if product present in database
        Product product = productRepository.findById(reviewDto.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product not found in database"));

        // Save review
        Review review = reviewRepository.save(ReviewConverter.reviewDtoToReview(reviewDto));

        // Update Product Ratings
        product.setTotalRating(product.getTotalRating() + reviewDto.getRating());
        product.setTotalReviews(product.getTotalReviews() + 1);

        float newRating = (float) product.getTotalRating() / product.getTotalReviews();
        product.setRating((float) (Math.round(newRating * 10) / 10.0));

        product.getReviews().add(review);

        productRepository.save(product);

        return ReviewConverter.reviewToReviewDto(review);
    }

    @Override
    public DeletedDto deleteReview(String reviewId) {
        // Check if review present in database
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found in database"));

        // Check if product present in database
        Product product = productRepository.findById(review.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found in database"));

        // Update Product Ratings
        product.setTotalRating(product.getTotalRating() - review.getRating());
        product.setTotalReviews(product.getTotalReviews() - 1);

        if (product.getTotalReviews() > 0) {
            float newRating = (float) product.getTotalRating() / product.getTotalReviews();
            product.setRating((float) (Math.round(newRating * 10) / 10.0));
        } else {
            product.setRating(0.0f);
        }

        // Remove review from product reviews list
        product.getReviews().removeIf(r -> r.getId().equals(reviewId));

        productRepository.save(product);

        reviewRepository.delete(review);

        return new DeletedDto(true, "Review deleted successfully");
    }
}
