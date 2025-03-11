package com.reviews.repository;

import com.reviews.entities.Review;
import com.reviews.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewRepositoryTest {

    @Mock
    private ReviewRepository reviewRepository;

    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        review1 = new Review("1", "Great product!", "101", 5, "user1@example.com");
        review2 = new Review("2", "Average product.", "101", 3, "user2@example.com");
    }

    @Test
    void testFindByProductId() {
        // Arrange
        List<Review> reviews = Arrays.asList(review1, review2);
        Page<Review> reviewPage = new PageImpl<>(reviews);
        Pageable pageable = Pageable.unpaged();

        when(reviewRepository.findByProductId(eq("101"), any(Pageable.class))).thenReturn(reviewPage);

        // Act
        Page<Review> result = reviewRepository.findByProductId("101", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(review1));
        assertTrue(result.getContent().contains(review2));
        verify(reviewRepository, times(1)).findByProductId("101", pageable);
    }

    @Test
    void testFindByUserEmail() {
        // Arrange
        List<Review> reviews = Arrays.asList(review1);
        Page<Review> reviewPage = new PageImpl<>(reviews);
        Pageable pageable = Pageable.unpaged();

        when(reviewRepository.findByUserEmail(eq("user1@example.com"), any(Pageable.class))).thenReturn(reviewPage);

        // Act
        Page<Review> result = reviewRepository.findByUserEmail("user1@example.com", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(review1));
        verify(reviewRepository, times(1)).findByUserEmail("user1@example.com", pageable);
    }
}