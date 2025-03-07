package com.reviews.controller;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;
import com.reviews.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<List<ReviewDto>> getProductReviews(@PathVariable String productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @GetMapping("user")
    public ResponseEntity<List<ReviewDto>> getUserReviews(@RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(reviewService.getUserReviews(userEmail));
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @PostMapping("")
    public ResponseEntity<ReviewDto> addReview(@RequestBody @Valid ReviewDto reviewDto, @RequestHeader("X-User-Email") String userEmail) {
        reviewDto.setUserEmail(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(reviewDto));
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<DeletedDto> deleteReview(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }
}
