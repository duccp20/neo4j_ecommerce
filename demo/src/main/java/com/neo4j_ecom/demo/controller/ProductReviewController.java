package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/products")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/{productId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ProductReviewResponse>> createProductReview(
            @PathVariable String productId,
            @Valid
            @RequestBody ProductReviewRequest request
    ) {

        log.info("create product review request : productId {}, request {}",  productId, request);

        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductReviewResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productReviewService.createReview(productId, request))
                        .build()
        );
    }

    @GetMapping("{productId}/reviews")
    public ResponseEntity<ApiResponse<PaginationResponse>> getAllReviewsByProductId(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder

    ) {

        Integer pageInt = Integer.parseInt(page);
        Integer sizeInt = Integer.parseInt(size);

        try {
            Integer.parseInt(page);
            Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        if (pageInt < 0 || sizeInt < 0) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        log.info("get all reviews by product request : {}, page {}, size {}", productId, page, size, sortBy, sortOrder);
        SuccessCode successCode = SuccessCode.FETCHED;

        PaginationResponse response = productReviewService.getAllReviewsByProductId(productId, pageInt, sizeInt, sortBy, sortOrder);

        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<PaginationResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(response)
                        .build()
        );
    }
    //
////    @PutMapping("/{id}/")
////    public ResponseEntity<ApiResponse<ProductResponse>> handleUpdateProductReview() {
////        return null;
////    }
//
//
//
//
//
    @GetMapping("{productId}/reviews/filter")
    public ResponseEntity<ApiResponse<PaginationResponse>> getAllReviewsByVariantIdFilter(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size,
            @PathVariable String productId,
            @RequestParam(defaultValue = "5") String rating
    ) {

        Integer pageInt = Integer.parseInt(page);
        Integer sizeInt = Integer.parseInt(size);

        try {
            Integer.parseInt(page);
            Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        if (pageInt < 0 || sizeInt < 0) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        Integer ratingInt = Integer.parseInt(rating);
        log.info("get all reviews by product request : {}, rating {}", productId, rating);
        SuccessCode successCode = SuccessCode.FETCHED;

        PaginationResponse response = productReviewService.getAllReviewsByProductIdFilter(productId, ratingInt, pageInt, sizeInt);
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<PaginationResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(response)
                        .build()
        );
    }
}
