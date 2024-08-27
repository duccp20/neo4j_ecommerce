package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
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

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ApiResponse<ProductResponse>> handleCreateProductReview(
            @PathVariable String productId,
            @Valid
            @RequestBody ProductReviewRequest request
    ) {

        log.info("create product review request : productId {}, request {}",  productId, request);

        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productReviewService.createReview(productId, request))
                        .build()
        );
    }

    @GetMapping("{productId}/reviews")
    public ResponseEntity<ApiResponse<ProductResponse>> handleGetAllReviewsByProductId(
            @PathVariable String productId
    ) {

        log.info("get all reviews by product request : {}", productId);
        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productReviewService.getAllReviewsByProductId(productId))
                        .build()
        );
    }

    @PutMapping("/{id}/")
    public ResponseEntity<ApiResponse<ProductResponse>> handleUpdateProductReview() {


        return null;
    }
}
