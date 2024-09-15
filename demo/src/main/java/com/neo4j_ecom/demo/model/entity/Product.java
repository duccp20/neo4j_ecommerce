package com.neo4j_ecom.demo.model.entity;


import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("products")
public class Product {

    @Id
    private String id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private BigDecimal sellingPrice;
    private String description;
    private Float rating;
    private long sumSoldQuantity;
    private long quantityAvailable;
    private SellingType sellingType;
    private String SKU;
    private String brandName;
    private List<String> productImages;
    private String primaryImage;
    private ProductDimension productDimension;
    private List<Category> categories;
    private List<ProductReview> reviews;
    private List<ProductBanner> productBanners;

    @DocumentReference
    private List<ProductVariant> productVariants;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

}
