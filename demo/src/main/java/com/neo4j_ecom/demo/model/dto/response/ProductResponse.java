package com.neo4j_ecom.demo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.entity.ProductDimension;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.*;
import lombok.experimental.FieldDefaults;



import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
    long soldQuantity;
    long quantityAvailable;
    String brandName;
    String description;
    ProductDimension productDimension;
    SellingType sellingType;
    String SKU;
    Float rating;
    List<CategoryResponse> categories;
    List<String> images;
    String primaryImage;
    List<ReviewResponse> reviews;
    List<ProductVariant> productVariants;
    Boolean hasVariants;
    List<ProductBannerResponse> productBanners;
    Instant createdAt;
    Instant updatedAt;
}
