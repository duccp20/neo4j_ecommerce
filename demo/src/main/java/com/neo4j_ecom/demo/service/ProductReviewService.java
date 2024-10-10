package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;

import java.util.List;

public interface ProductReviewService {

    ProductReviewResponse createReview(String productId, ProductReviewRequest review);

    ProductReviewResponse updateReview(String productId, String reviewId, ProductReviewRequest reviewRequest);

    PaginationResponse getAllReviewsByProductId(String productId, int page, int size, String sortBy, String sortOrder);

    PaginationResponse getAllReviewsByProductIdFilter(String productId, int rating, int page, int size);

    void deleteReview(String productId, String reviewId);

}
