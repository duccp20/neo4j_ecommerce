package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ReviewResponse;
import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.repository.ProductReviewRepository;
import com.neo4j_ecom.demo.repository.ProductVariantRepository;
import com.neo4j_ecom.demo.service.Authentication.Impl.AccountServiceImpl;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.service.UserServices;
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

    private final UserServices userServices;

    @Override
    public ProductReviewResponse createReview(String productId, ProductReviewRequest review) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("Product found: {}", product);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userServices.getUserByEmail(email).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        ProductReview productReview = reviewMapper.toEntity(review);
        productReview.setProduct(product);
        productReview.setReviewer(user);
        ProductReview savedProductReview = productReviewRepository.save(productReview);


        //add review to product
        List<ProductReview> reviews = product.getReviews();
        reviews.add(savedProductReview);

        product.setReviews(reviews);

        //calculate avg rating
        float avgRating = this.calculateRating(reviews);
        log.info("Average rating: {}", avgRating);
        product.setAvgRating(avgRating);
        product.setCountOfReviews(reviews.size());

        productRepository.save(product);

        return reviewMapper.toResponse(savedProductReview);
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

        //map entity to response
        List<ProductReviewResponse> reviewResponses = new ArrayList<>();
        reviews.forEach(review -> {
            reviewResponses.add(reviewMapper.toResponse(review));
        });

        //review response with total reviews, avg rating
        ReviewResponse reviewResponse = ReviewResponse.builder()
                .countOfReviews(totalReviews)
                .avgRating(avgRating)
                .reviews(reviewResponses)
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

        List<ProductReviewResponse> reviewResponses = reviewPage.getContent().stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());

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
                        .reviews(reviewResponses)
                        .build())
                .build();
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
