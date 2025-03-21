package com.reviews.controller;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;
import com.reviews.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<ReviewDto>> getProductReviews(@PathVariable String productId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId, page, size));
    }

    @GetMapping("user")
    public ResponseEntity<Page<ReviewDto>> getUserReviews(@RequestHeader("X-User-Email") String userEmail, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getUserReviews(userEmail, page, size));
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
