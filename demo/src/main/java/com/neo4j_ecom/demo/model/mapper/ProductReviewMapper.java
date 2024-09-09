package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;

import com.neo4j_ecom.demo.model.entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
public class ProductReviewMapper {


    public ReviewResponse toResponse(ProductReview review) {
        return ReviewResponse.builder()
                .id(review.getId() != null ? review.getId() : null)
                .content(review.getContent() != null ? review.getContent() : null)
                .rating(review.getRating() >= 0 ? review.getRating() : 0)
                .build();
    }

    public ProductReview toEntity(ProductReviewRequest request) {

        ProductReview review = new ProductReview();
        review.setContent(request.getContent() != null ? request.getContent() : null);
        review.setRating(request.getRating() >= 0 ? request.getRating() : 0);
        return review;
    }
}
