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
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
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

    private final ProductReviewMapper reviewMapper;

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
                .build();

        ProductReview savedReview = productReviewRepository.save(productReview);

        log.info("Saved review: {}", savedReview);

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
                .reviewerId(savedReview.getReviewer().getId())
                .reviewerName(savedReview.getReviewer().getFirstName() + " " + savedReview.getReviewer().getLastName())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .build();
    }

    @Override
    public ProductReviewResponse updateReview(String productId, String reviewId, ProductReviewRequest reviewRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductReview productReview = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        productReview.setContent(reviewRequest.getContent());
        productReview.setRating(reviewRequest.getRating());
        productReview.setName(reviewRequest.getName());
        productReview.setEmail(reviewRequest.getEmail());
        productReview.setTitle(reviewRequest.getTitle());
        productReview.setOptions(reviewRequest.getOptions());

        ProductReview updatedReview = productReviewRepository.save(productReview);

        List<ProductReview> reviews = product.getReviews();
        reviews = reviews.stream()
                .map(review -> review.getId().equals(reviewId) ? updatedReview : review)
                .collect(Collectors.toList());
        product.setReviews(reviews);

        float avgRating = this.calculateRating(reviews);
        product.setAvgRating(avgRating);
        product.setCountOfReviews(reviews.size());

        productRepository.save(product);

        return ProductReviewResponse.builder()
                .id(updatedReview.getId())
                .content(updatedReview.getContent())
                .rating(updatedReview.getRating())
                .name(updatedReview.getName())
                .email(updatedReview.getEmail())
                .title(updatedReview.getTitle())
                .options(updatedReview.getOptions())
                .reviewerId(updatedReview.getReviewer() != null ? updatedReview.getReviewer().getId() : null)
                .reviewerName(updatedReview.getReviewer() != null ? updatedReview.getReviewer().getFirstName() + " " + updatedReview.getReviewer().getLastName() : null)
                .createdAt(updatedReview.getCreatedAt())
                .updatedAt(updatedReview.getUpdatedAt())
                .build();
    }

    @Override
    public PaginationResponse getAllReviewsByProductId(String productId, int page, int size, String sortBy, String sortOrder) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<String> reviewIds = product.getReviews().stream()
                .map(ProductReview::getId)
                .collect(Collectors.toList());

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<ProductReview> reviewPage = productReviewRepository.findAllByIds(reviewIds, pageRequest);

        List<ProductReviewResponse> reviewResponses = reviewPage.getContent().stream().map(review -> {
            return ProductReviewResponse.builder()
                    .id(review.getId())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .name(review.getName())
                    .email(review.getEmail())
                    .title(review.getTitle())
                    .options(review.getOptions())
                    .reviewerId(review.getReviewer() != null ? review.getReviewer().getId() : null)
                    .reviewerName(review.getReviewer() != null ? review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName() : null)
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());

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
                        .countOfReviews(product.getCountOfReviews())
                        .avgRating(product.getAvgRating())
                        .reviews(reviewResponses)
                        .build())
                .build();
    }


    @Override
    public PaginationResponse getAllReviewsByProductIdFilter(String productId, int rating, int page, int size) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<String> reviewIds = product.getReviews().stream()
                .map(ProductReview::getId)
                .collect(Collectors.toList());

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ProductReview> reviewPage = productReviewRepository.findAllByIdsAndRating(reviewIds, rating, pageRequest);

        List<ProductReviewResponse> reviewResponses = reviewPage.getContent().stream().map(review -> {
            return ProductReviewResponse.builder()
                    .id(review.getId())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .name(review.getName())
                    .email(review.getEmail())
                    .title(review.getTitle())
                    .options(review.getOptions())
                    .reviewerId(review.getReviewer() != null ? review.getReviewer().getId() : null)
                    .reviewerName(review.getReviewer() != null ? review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName() : null)
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());

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
                        .countOfReviews(product.getCountOfReviews())
                        .avgRating(product.getAvgRating())
                        .reviews(reviewResponses)
                        .build())
                .build();

    }

    @Override
    public void deleteReview(String productId, String reviewId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductReview productReview = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        productReviewRepository.delete(productReview);

        List<ProductReview> reviews = product.getReviews();
        reviews.removeIf(review -> review.getId().equals(reviewId));
        product.setReviews(reviews);

        float avgRating = this.calculateRating(reviews);
        product.setAvgRating(avgRating);
        product.setCountOfReviews(reviews.size());

        productRepository.save(product);
    }

    private float calculateRating(List<ProductReview> reviews) {
        float totalRating = 0;
        float avgRating = 0;
        for (ProductReview productReview1 : reviews) {

            totalRating += productReview1.getRating();

        }

        if (reviews.size() > 0) {
            avgRating = totalRating / reviews.size();
        }

        return avgRating;
    }
}
