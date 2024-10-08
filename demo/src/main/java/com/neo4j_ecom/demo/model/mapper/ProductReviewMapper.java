package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;

import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
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

    public ProductReviewResponse toResponse(ProductReview savedProductReview) {
        return ProductReviewResponse.builder()
                .content(savedProductReview.getContent())
                .rating(savedProductReview.getRating())
                .name(savedProductReview.getName())
                .email(savedProductReview.getEmail())
                .title(savedProductReview.getTitle())
                .options(savedProductReview.getOptions())
                .id(savedProductReview.getId())
                .productId(savedProductReview.getProduct().getId())
                .createdAt(savedProductReview.getCreatedAt())
                .updatedAt(savedProductReview.getUpdatedAt())
                .reviewerId(savedProductReview.getReviewer() != null ? savedProductReview.getReviewer().getId() : null)
                .reviewerName(savedProductReview.getReviewer() != null ? savedProductReview.getReviewer().getFullName() : null)
                .build();
    }
}
