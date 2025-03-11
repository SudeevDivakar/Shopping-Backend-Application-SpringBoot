package com.reviews.controller;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;
import com.reviews.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDto reviewDto;

    @BeforeEach
    void setUp() {
        reviewDto = new ReviewDto("1", "Great product!", "p123", 5, "user@example.com");
    }

    @Test
    void testGetAllReviews() {
        List<ReviewDto> reviews = List.of(reviewDto);
        when(reviewService.getAllReviews()).thenReturn(reviews);

        ResponseEntity<List<ReviewDto>> response = reviewController.getAllReviews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
    }

    @Test
    void testGetProductReviews() {
        Page<ReviewDto> page = new PageImpl<>(List.of(reviewDto));
        when(reviewService.getProductReviews("p123", 0, 10)).thenReturn(page);

        ResponseEntity<Page<ReviewDto>> response = reviewController.getProductReviews("p123", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void testGetUserReviews() {
        Page<ReviewDto> page = new PageImpl<>(List.of(reviewDto));
        when(reviewService.getUserReviews("user@example.com", 0, 10)).thenReturn(page);

        ResponseEntity<Page<ReviewDto>> response = reviewController.getUserReviews("user@example.com", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void testGetReview() {
        when(reviewService.getReview("1")).thenReturn(reviewDto);

        ResponseEntity<ReviewDto> response = reviewController.getReview("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDto, response.getBody());
    }

    @Test
    void testAddReview() {
        when(reviewService.addReview(any(ReviewDto.class))).thenReturn(reviewDto);

        ResponseEntity<ReviewDto> response = reviewController.addReview(reviewDto, "user@example.com");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reviewDto, response.getBody());
    }

    @Test
    void testDeleteReview() {
        DeletedDto deletedDto = new DeletedDto(true, "Review deleted successfully");
        when(reviewService.deleteReview("1")).thenReturn(deletedDto);

        ResponseEntity<DeletedDto> response = reviewController.deleteReview("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedDto, response.getBody());
    }
}

