package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductReviewRepository extends MongoRepository<ProductReview, String> {

}
