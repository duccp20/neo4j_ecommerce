package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.repository.ProductReviewRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReviewServiceImpl implements ProductReviewService {


    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override

    public ProductReview createReview(String productId, ProductReviewRequest reviewRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("Product found: {}", product);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.getUserByEmail(email).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        ProductReview productReview = ProductReview.fromRequest(reviewRequest);
        productReview.setReviewer(user);

        ProductReview savedReview = productReviewRepository.save(productReview);

        log.info("Saved review: {}", savedReview);


        List<ProductReview> listReviews = product.getReviews();
        listReviews.add(productReview);

        float avgRating = this.calculateRating(listReviews);

        product.setAvgRating(avgRating);
        product.setCountOfReviews(listReviews.size());
        productRepository.save(product);

        return savedReview;

    }

    @Override
    public ProductReview updateReview(String productId, String reviewId, ProductReviewRequest reviewRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductReview existingReview = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        ProductReview updatedReview = ProductReview.fromRequest(reviewRequest);
        updatedReview.setId(existingReview.getId());  // Giữ nguyên ID
        updatedReview.setReviewer(existingReview.getReviewer());  // Giữ nguyên reviewer
        updatedReview.setCreatedAt(existingReview.getCreatedAt());  // Giữ nguyên thời điểm tạo

        ProductReview savedReview = productReviewRepository.save(updatedReview);

        List<ProductReview> reviews = product.getReviews();
        reviews = reviews.stream()
                .map(review -> review.getId().equals(reviewId) ? savedReview : review)
                .collect(Collectors.toList());
        product.setReviews(reviews);

        float avgRating = this.calculateRating(reviews);
        product.setAvgRating(avgRating);
        product.setCountOfReviews(reviews.size());

        productRepository.save(product);

        return savedReview;
    }


    @Override
    public PaginationResponse getAllReviewsByProductId(String productId, int page, int size, String sortBy, String sortOrder) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        int totalReviews = product.getCountOfReviews();
        float avgRating = product.getAvgRating();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));

        List<String> reviewIds = product.getReviews().stream()
                .map(ProductReview::getId).collect(Collectors.toList());

        Page<ProductReview> reviewPage = productReviewRepository.findByIdIn(reviewIds, pageRequest);

        return PaginationResponse.builder()
                .meta(Meta.fromPage(reviewPage))
                .result(ReviewResponse.builder()
                        .countOfReviews(totalReviews)
                        .avgRating(avgRating)
                        .reviews(reviewPage.getContent())
                        .build())
                .build();
    }

    @Override
    public PaginationResponse getAllReviewsByProductIdFilter(String productId, int rating, int page, int size) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        float avgRating = product.getAvgRating();
        int totalReviews = product.getCountOfReviews();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "rating"));

        List<String> reviewIds = product.getReviews().stream()
                .map(ProductReview::getId).collect(Collectors.toList());

        Page<ProductReview> reviewPage = productReviewRepository.findByIdIn(reviewIds, pageRequest);

        return PaginationResponse.builder()
                .meta(Meta.fromPage(reviewPage))
                .result(ReviewResponse.builder()
                        .countOfReviews(totalReviews)
                        .avgRating(avgRating)
                        .reviews(reviewPage.getContent())
                        .build())
                .build();
    }

    @Override
    public Void deleteReview(String productId, String reviewId) {

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

        return null;
    }

    private float calculateRating(List<ProductReview> reviews) {
        return reviews.isEmpty() ? 0 : (float) reviews.stream()
                .mapToDouble(ProductReview::getRating)
                .sum() / reviews.size();
    }
}
