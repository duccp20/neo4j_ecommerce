package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductReview;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.repository.ProductReviewRepository;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReviewServiceImpl implements ProductReviewService {


    private final ProductReviewRepository productReviewRepository;

    private final ProductRepository productRepository;

    private final ProductReviewMapper reviewMapper;

    private final ProductMapper productMapper;

    @Override
    public ProductResponse createReview(String productId, ProductReviewRequest review) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("Product found: {}", product);

        ProductReview productReview = reviewMapper.toEntity(review);

        List<ProductReview> reviews = product.getReviews();
        reviews.add(productReview);

        product.setReviews(reviews);

        double avgRating = this.calculateRating(reviews);

        log.info("Average rating: {}", avgRating);

        product.setRating(avgRating);

        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    private double calculateRating(List<ProductReview> reviews) {
        double totalRating = 0;
        double avgRating = 0;
        for (ProductReview productReview1 : reviews) {

            totalRating += productReview1.getRating();

        }

        if(reviews.size() > 0) {
           avgRating = totalRating / reviews.size();
        }

        return avgRating;
    }


}
