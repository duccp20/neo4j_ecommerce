package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductReviewRepository extends MongoRepository<ProductReview, String> {

//    Page<ProductReview> (String productId, PageRequest pageRequest);
//
//    Page<ProductReview> findAllByProductIdAndRating(String productId, int rating, PageRequest pageRequest);
}
