package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
//import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.repository.ProductReviewRepository;
import com.neo4j_ecom.demo.repository.ProductVariantRepository;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.service.UserService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReviewServiceImpl implements ProductReviewService {


    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

//    private final ProductReviewMapper reviewMapper;

    private final ProductMapper productMapper;

    private final UserService userService;

    @Override
    public ProductReviewResponse createReview(String productId, ProductReviewRequest reviewRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("Product found: {}", product);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.getUserByEmail(email).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        ProductReview productReview = ProductReview.builder()
                .name(reviewRequest.getName())
                .email(reviewRequest.getEmail())
                .title(reviewRequest.getTitle())
                .rating(reviewRequest.getRating())
                .content(reviewRequest.getContent())
                .options(reviewRequest.getOptions())
                .reviewer(user)
                .productId(productId)
                .build();

        ProductReview savedReview = productReviewRepository.save(productReview);

        List<ProductReview> reviews = product.getReviews();
        reviews.add(savedReview);
        product.setReviews(reviews);

        float avgRating = this.calculateRating(reviews);
        log.info("Average rating: {}", avgRating);
        product.setAvgRating(avgRating);
        product.setCountOfReviews(reviews.size());

        productRepository.save(product);

        return ProductReviewResponse.builder()
                .id(savedReview.getId())
                .content(savedReview.getContent())
                .rating(savedReview.getRating())
                .name(savedReview.getName())
                .email(savedReview.getEmail())
                .title(savedReview.getTitle())
                .options(savedReview.getOptions())
                .productId(savedReview.getProductId())
                .reviewerId(savedReview.getReviewer().getId())
                .reviewerName(savedReview.getReviewer().getFirstName() + " " + savedReview.getReviewer().getLastName())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .build();
    }

    @Override
    public PaginationResponse getAllReviewsByProductId(String productId, int page, int size, String sortBy, String sortOrder) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        int totalReviews = product.getCountOfReviews();
        float avgRating = product.getAvgRating();

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        //page
        Page<ProductReview> reviewPage = productReviewRepository.findAllByProductId(productId, pageRequest);

        //reviews entities
        List<ProductReview> reviews = reviewPage.getContent();

//        //map entity to response
//        List<ProductReviewResponse> reviewResponses = new ArrayList<>();
//        reviews.forEach(review -> {
//            reviewResponses.add(reviewMapper.toResponse(review));
//        });

        //review response with total reviews, avg rating
        ReviewResponse reviewResponse = ReviewResponse.builder()
                .countOfReviews(totalReviews)
                .avgRating(avgRating)
                .reviews(reviews)
                .build();

        Meta meta = Meta.builder()
                .current(reviewPage.getNumber() + 1)
                .pageSize(reviewPage.getNumberOfElements())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(reviewPage.getTotalElements())
                .isFirstPage(reviewPage.isFirst())
                .isLastPage(reviewPage.isLast())
                .build();

        log.info("reviews by product id: {}", reviewPage);


        return PaginationResponse.builder()
                .meta(meta)
                .result(reviewResponse)
                .build();
    }

    @Override
    public PaginationResponse getAllReviewsByProductIdFilter(String productId, int rating, int page, int size) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        float avgRating = product.getAvgRating();
        int totalReviews = product.getCountOfReviews();

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ProductReview> reviewPage = productReviewRepository.findAllByProductIdAndRating(productId, rating, pageRequest);

        List<ProductReview> reviews = reviewPage.getContent();

        Meta meta = Meta.builder()
                .current(reviewPage.getNumber() + 1)
                .pageSize(reviewPage.getNumberOfElements())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(reviewPage.getTotalElements())
                .isFirstPage(reviewPage.isFirst())
                .isLastPage(reviewPage.isLast())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .result(ReviewResponse.builder()
                        .countOfReviews(totalReviews)
                        .avgRating(avgRating)
                        .reviews(reviews)
                        .build())
                .build();
    }


    private float calculateRating(List<ProductReview> reviews) {
        float totalRating = 0;
        for (ProductReview review : reviews) {
            totalRating += review.getRating();
        }

        return reviews.isEmpty() ? 0 : totalRating / reviews.size();
    }
}
