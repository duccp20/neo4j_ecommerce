package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;

import java.util.List;

public interface ProductReviewService {

    ProductResponse createReview(String productId, ProductReviewRequest review);



}
