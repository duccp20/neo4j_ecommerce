package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.repository.ProductReviewRepository;
import com.neo4j_ecom.demo.repository.ProductVariantRepository;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Override
    public ProductReview createReview(String variantId, ProductReviewRequest review) {

//        ProductVariant productVariant = variantRepository.findById(variantId)
//                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        log.info("Product found: {}", productVariant);
//
//        ProductReview productReview = reviewMapper.toEntity(review);
//        productReview.setVariantId(variantId);
//
//        //missing filed user...do it later
//
//        List<ProductReview> reviews = productVariant.getReviews();
//        reviews.add(productReviewRepository.save(productReview));
//
//        productVariant.setReviews(reviews);
//
//        float avgRating = this.calculateRating(reviews);
//
//        log.info("Average rating: {}", avgRating);
//
//        productVariant.setAvgRating(avgRating);
//        productVariant.setCountOfReviews(reviews.size());
//
//        variantRepository.save(productVariant);
//
//        return productReview;

        return null;

    }

    @Override
    public ProductReview getAllReviewsByProductId(String productId) {
        return null;
    }

    @Override
    public ReviewResponse getAllReviewsByVariantId(String variantId) {

//        ProductVariant productVariant = variantRepository.findById(variantId)
//                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        ReviewResponse reviewResponse = new ReviewResponse();
//        reviewResponse.setAvgRating(productVariant.getAvgRating());
//        reviewResponse.setCountOfReviews(productVariant.getCountOfReviews());
//        reviewResponse.setReviews(productVariant.getReviews());
//
//        return reviewResponse;

        return null;
    }

    @Override
    public ReviewResponse getAllReviewsByVariantIdSort(String variantId, String sortBy, String order) {
        ReviewResponse reviewResponse = this.getAllReviewsByVariantId(variantId);
        List<ProductReview> reviews = reviewResponse.getReviews().stream()
                .sorted(getComparator(sortBy, order))
                .collect(Collectors.toList());
        reviewResponse.setReviews(reviews);
        return reviewResponse;
    }

    @Override
    public ReviewResponse getAllReviewsByVariantIdFilter(String variantId, int rating) {
        ProductVariant productVariant = variantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

//        List<ProductReview> reviews = productVariant.getReviews().stream()
//                .filter(review -> review.getRating() == rating)
//                .collect(Collectors.toList());
//
//        ReviewResponse reviewResponse = new ReviewResponse();
//        reviewResponse.setAvgRating(this.calculateRating(reviews));
//        reviewResponse.setCountOfReviews(reviews.size());
//        reviewResponse.setReviews(reviews);

//        return reviewResponse;

        return null;
    }

    private Comparator<ProductReview> getComparator(String sortBy, String order) {
        Comparator<ProductReview> comparator;
        switch (sortBy) {
            case "rating":
                comparator = Comparator.comparingInt(ProductReview::getRating);
                break;
            case "date":
                comparator = Comparator.comparing(ProductReview::getUpdatedAt);
                break;
            default:
                throw new AppException(ErrorCode.INVALID_SORT_BY);
        }

        if (order.equalsIgnoreCase("desc")) {
            return comparator.reversed();
        } else if (order.equalsIgnoreCase("asc")) {
            return comparator;
        } else {
            throw new AppException(ErrorCode.INVALID_SORT_ORDER);
        }
    }

    private float calculateRating(List<ProductReview> reviews) {
        float totalRating = 0;
        float avgRating = 0;
        for (ProductReview productReview1 : reviews) {

            totalRating += productReview1.getRating();

        }

        if(reviews.size() > 0) {
           avgRating = totalRating / reviews.size();
        }

        return avgRating;
    }
}
