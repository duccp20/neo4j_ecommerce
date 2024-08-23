package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Review;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {


    @Override
    public ProductResponse createReview(ProductReviewRequest review) {
        return null;
    }
}
