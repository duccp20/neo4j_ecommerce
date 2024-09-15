package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;

import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import org.springframework.stereotype.Component;

@Component
public class ProductReviewMapper {


    public ProductReview toEntity(ProductReviewRequest request) {

        ProductReview review = new ProductReview();
        review.setContent(request.getContent() != null ? request.getContent() : null);
        review.setRating(request.getRating() >= 0 ? request.getRating() : 0);
        review.setName(request.getName() != null ? request.getName() : null);
        review.setEmail(request.getEmail() != null ? request.getEmail() : null);
        review.setTitle(request.getTitle() != null ? request.getTitle() : null);
        review.setOptions(request.getOptions() != null ? request.getOptions() : null);

        return review;
    }
}
