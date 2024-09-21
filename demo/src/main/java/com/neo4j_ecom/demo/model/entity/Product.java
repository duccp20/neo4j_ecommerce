package com.neo4j_ecom.demo.model.entity;


import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
    private Float avgRating;
    private long quantityAvailable;
    private SellingType sellingType;
    private String SKU;
    private List<String> productImages = new ArrayList<>();
    private String primaryImage;
    private ProductDimension productDimension;
    private List<ProductBanner> productBanners = new ArrayList<>();
    @DocumentReference(lazy = true)
    private Brand brand;
    @DocumentReference(lazy = true)
    private List<Category> categories = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<ProductReview> reviews = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<ProductVariant> productVariants;
    private List<SpecificationOption> productSpecifications;
    private int countOfReviews;
    private long sumSoldQuantity;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

}
