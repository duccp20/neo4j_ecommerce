package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.entity.ProductReview;
import com.neo4j_ecom.demo.model.entity.Review;

public interface ProductReviewService {

    ProductReview createReview(ProductReviewRequest review);


}
