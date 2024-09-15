package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;

public interface ProductReviewService {

    ProductReview createReview(String productId, ProductReviewRequest review);

    ProductReview getAllReviewsByProductId(String productId);

    ReviewResponse getAllReviewsByVariantId(String variantId);

    ReviewResponse getAllReviewsByVariantIdSort(String variantId, String sortBy, String sortOrder);
}
