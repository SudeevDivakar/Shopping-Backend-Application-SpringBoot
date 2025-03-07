package com.reviews.repositories;

import com.reviews.entities.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    public List<Review> findByProductId(String productId);
    public List<Review> findByUserEmail(String userEmail);
}
