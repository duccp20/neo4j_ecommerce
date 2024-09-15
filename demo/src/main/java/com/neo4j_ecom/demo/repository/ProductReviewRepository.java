package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductReviewRepository extends MongoRepository<ProductReview, String> {

}
