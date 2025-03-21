package com.reviews.repositories;

import com.reviews.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    public Page<Review> findByProductId(String productId, Pageable pageable);
    public Page<Review> findByUserEmail(String userEmail, Pageable pageable);
}
