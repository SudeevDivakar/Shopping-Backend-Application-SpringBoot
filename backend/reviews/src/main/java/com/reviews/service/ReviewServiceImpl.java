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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("Fetching all reviews");
        return reviewRepository.findAll().stream().map(ReviewConverter::reviewToReviewDto).toList();
    }

    @Override
    public Page<ReviewDto> getProductReviews(String productId, int page, int size) {
        log.info("Fetching reviews for product ID: {}", productId);
        productRepository.findById(productId).orElseThrow(() -> {
            log.error("Product with ID {} not found", productId);
            return new ProductNotFoundException("Product not found in database");
        });

        Pageable pageable = PageRequest.of(page, size);

        return reviewRepository.findByProductId(productId, pageable)
                .map(ReviewConverter::reviewToReviewDto);
    }

    @Override
    public Page<ReviewDto> getUserReviews(String userEmail, int page, int size) {
        log.info("Fetching reviews for user: {}", userEmail);
        Pageable pageable = PageRequest.of(page, size);

        Page<ReviewDto> reviews = reviewRepository.findByUserEmail(userEmail, pageable)
                .map(ReviewConverter::reviewToReviewDto);

        if (reviews.isEmpty()) {
            log.warn("No reviews found for user: {}", userEmail);
            throw new ReviewNotFoundException("User has not added any reviews");
        }
        return reviews;
    }

    @Override
    public ReviewDto getReview(String reviewId) {
        log.info("Fetching review with ID: {}", reviewId);
        return ReviewConverter.reviewToReviewDto(reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error("Review with ID {} not found", reviewId);
            return new ReviewNotFoundException("Review not found in database");
        }));
    }

    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        log.info("Adding a new review for product ID: {}", reviewDto.getProductId());
        // Check if product present in database
        Product product = productRepository.findById(reviewDto.getProductId()).orElseThrow(() -> {
            log.error("Product with ID {} not found", reviewDto.getProductId());
            return new ProductNotFoundException("Product not found in database");
        });

        // Save review
        Review review = reviewRepository.save(ReviewConverter.reviewDtoToReview(reviewDto));
        log.info("Review saved with ID: {}", review.getId());

        // Update Product Ratings
        product.setTotalRating(product.getTotalRating() + reviewDto.getRating());
        product.setTotalReviews(product.getTotalReviews() + 1);

        float newRating = (float) product.getTotalRating() / product.getTotalReviews();
        product.setRating((float) (Math.round(newRating * 10) / 10.0));

        product.getReviews().add(review);

        productRepository.save(product);
        log.info("Updated product rating for product ID: {}", reviewDto.getProductId());

        return ReviewConverter.reviewToReviewDto(review);
    }

    @Override
    public DeletedDto deleteReview(String reviewId) {
        log.info("Deleting review with ID: {}", reviewId);
        // Check if review present in database
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review with ID {} not found", reviewId);
                    return new ReviewNotFoundException("Review not found in database");
                });

        // Check if product present in database
        Product product = productRepository.findById(review.getProductId())
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", review.getProductId());
                    return new ProductNotFoundException("Product not found in database");
                });

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
        log.info("Updated product rating after review deletion for product ID: {}", review.getProductId());

        reviewRepository.delete(review);
        log.info("Review with ID {} deleted successfully", reviewId);

        return new DeletedDto(true, "Review deleted successfully");
    }
}
