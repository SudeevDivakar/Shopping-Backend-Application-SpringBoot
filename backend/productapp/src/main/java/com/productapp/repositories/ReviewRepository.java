package com.productapp.repositories;

import com.productapp.entities.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    public void deleteByProductId(String productId);
}
