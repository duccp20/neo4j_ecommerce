package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/products")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ApiResponse<ProductReviewResponse>> handleCreateProductReview(
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
    public ResponseEntity<ApiResponse<PaginationResponse>> handleGetAllReviewsByProductId(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "UpdatedAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder

    ) {

        log.info("get all reviews by product request : {}, page {}, size {}", productId, page, size, sortBy, sortOrder);
        SuccessCode successCode = SuccessCode.FETCHED;

        PaginationResponse response = productReviewService.getAllReviewsByProductId(productId, page, size, sortBy, sortOrder);

        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<PaginationResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(response)
                        .build()
        );
    }

//    @PutMapping("/{id}/")
//    public ResponseEntity<ApiResponse<ProductResponse>> handleUpdateProductReview() {
//        return null;
//    }





    @GetMapping("{productId}/reviews/filter")
    public ResponseEntity<ApiResponse<PaginationResponse>> handleGetAllReviewsByVariantIdFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable String productId,
            @RequestParam(defaultValue = "5") String rating
    ) {

        Integer ratingInt = Integer.parseInt(rating);
        log.info("get all reviews by product request : {}, rating {}", productId, rating);
        SuccessCode successCode = SuccessCode.FETCHED;

        PaginationResponse response = productReviewService.getAllReviewsByProductIdFilter(productId, ratingInt, page, size);
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<PaginationResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(response)
                        .build()
        );
    }
}
