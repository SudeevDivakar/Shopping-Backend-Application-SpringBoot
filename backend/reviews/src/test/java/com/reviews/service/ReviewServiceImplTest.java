package com.reviews.service;

import com.reviews.dto.DeletedDto;
import com.reviews.dto.ReviewDto;
import com.reviews.entities.Product;
import com.reviews.entities.Review;
import com.reviews.exceptions.ProductNotFoundException;
import com.reviews.exceptions.ReviewNotFoundException;
import com.reviews.repositories.ProductRepository;
import com.reviews.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;

    private Review review;
    private ReviewDto reviewDto;
    private Product product;

    @BeforeEach
    void setUp() {
        review = new Review("id", "Great product!", "productId", 5, "user@example.com");
        reviewDto = new ReviewDto("id", "Great product!", "productId", 5, "user@example.com");

        product = new Product("id", "Product Name", "Product Description", 5, 5.0,
                "image_url", 5.0f, 0L, 0L, new ArrayList<>(List.of(review)));
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));
        List<ReviewDto> result = reviewServiceImpl.getAllReviews();
        assertEquals(1, result.size());
    }

    @Test
    void testGetProductReviews_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);

        when(productRepository.findById("productId")).thenReturn(Optional.of(product));
        when(reviewRepository.findByProductId("productId", pageable)).thenReturn(reviewPage);

        // Act
        Page<ReviewDto> result = reviewServiceImpl.getProductReviews("productId", 0, 10);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Great product!", result.getContent().get(0).getReview());
    }

    @Test
    void testGetProductReviews_ProductNotFound() {
        when(productRepository.findById("productId")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> reviewServiceImpl.getProductReviews("productId", 0, 10));
    }

    @Test
    void testGetUserReviews_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);

        when(reviewRepository.findByUserEmail("user@example.com", pageable)).thenReturn(reviewPage);

        // Act
        Page<ReviewDto> result = reviewServiceImpl.getUserReviews("user@example.com", 0, 10);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Great product!", result.getContent().get(0).getReview());
    }

    @Test
    void testGetUserReviews_NoReviewsFound() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(), pageable, 0);

        when(reviewRepository.findByUserEmail("user@example.com", pageable)).thenReturn(reviewPage);

        // Act & Assert
        assertThrows(ReviewNotFoundException.class, () -> reviewServiceImpl.getUserReviews("user@example.com", 0, 10));
    }

    @Test
    void testGetReview_Success() {
        when(reviewRepository.findById("id")).thenReturn(Optional.of(review));
        ReviewDto result = reviewServiceImpl.getReview("id");
        assertEquals("Great product!", result.getReview());
    }

    @Test
    void testGetReview_NotFound() {
        when(reviewRepository.findById("id")).thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewServiceImpl.getReview("id"));
    }

    @Test
    void testAddReview_Success() {
        when(productRepository.findById("productId")).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        ReviewDto result = reviewServiceImpl.addReview(reviewDto);
        assertNotNull(result);
        assertEquals("Great product!", result.getReview());
    }

    @Test
    void testAddReview_ProductNotFound() {
        when(productRepository.findById("productId")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> reviewServiceImpl.addReview(reviewDto));
    }

    @Test
    void testDeleteReview_Success() {
        when(reviewRepository.findById("id")).thenReturn(Optional.of(review));
        when(productRepository.findById("productId")).thenReturn(Optional.of(product));
        DeletedDto result = reviewServiceImpl.deleteReview("id");
        verify(reviewRepository, times(1)).delete(any(Review.class));
        assertTrue(result.isDeleted());
    }

    @Test
    void testDeleteReview_ReviewNotFound() {
        when(reviewRepository.findById("id")).thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewServiceImpl.deleteReview("id"));
    }

    @Test
    void testDeleteReview_ProductNotFound() {
        when(reviewRepository.findById("id")).thenReturn(Optional.of(review));
        when(productRepository.findById("productId")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> reviewServiceImpl.deleteReview("id"));
    }
}