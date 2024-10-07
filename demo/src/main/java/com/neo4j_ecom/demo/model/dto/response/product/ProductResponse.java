package com.neo4j_ecom.demo.model.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.model.entity.ProductDimension;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.ReviewType;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.*;
import lombok.experimental.FieldDefaults;



import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    String id;
    String name;
    BigDecimal originalPrice;
    BigDecimal discountedPrice;
    BigDecimal sellingPrice;
    long sumSoldQuantity;
    long quantityAvailable;
    Brand brand;
    String description;
    ProductDimension productDimension;
    SellingType sellingType;
    String SKU;
    Float avgRating;
    Integer countOfReviews;
    List<Object> categories;
    List<String> images;
    String primaryImage;
    ProductType primaryVariantType;
    List<ProductVariant> productVariants;
    List<SpecificationOption> productSpecifications;
    Boolean hasVariants;
    Boolean hasDimensions;
    Boolean hasSpecification;
    Boolean hasReviews;

    List<ProductBannerResponse> productBanners;
    List<ReviewOption> reviewOptions = new ArrayList<>();
    Map<ProductType, Set<String>> options;
    Instant createdAt;
    Instant updatedAt;
}
