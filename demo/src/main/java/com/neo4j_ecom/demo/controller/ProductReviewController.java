package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/products")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/variants/{variantId}/reviews")
    public ResponseEntity<ApiResponse<ProductReview>> handleCreateProductReview(
            @PathVariable String variantId,
            @Valid
            @RequestBody ProductReviewRequest request
    ) {

        log.info("create product review request : productId {}, request {}",  variantId, request);

        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductReview>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productReviewService.createReview(variantId, request))
                        .build()
        );
    }

    @GetMapping("variants/{variantId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> handleGetAllReviewsByVariantId(
            @PathVariable String variantId
    ) {

        log.info("get all reviews by product request : {}", variantId);
        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ReviewResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productReviewService.getAllReviewsByVariantId(variantId))
                        .build()
        );
    }

    @PutMapping("/{id}/")
    public ResponseEntity<ApiResponse<ProductResponse>> handleUpdateProductReview() {


        return null;
    }
}
