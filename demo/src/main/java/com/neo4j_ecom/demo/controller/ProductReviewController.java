package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reviews/products")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> handleCreateProductReview(
            @PathVariable String productId,
            @Valid
            @RequestBody ProductReviewRequest request
    ) {

        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productReviewService.createReview(productId, request))
                        .build()
        );
    }




}
