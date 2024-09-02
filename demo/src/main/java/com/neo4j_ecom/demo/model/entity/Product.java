package com.neo4j_ecom.demo.model.entity;


import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Node
public class Product {

    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private BigDecimal sellingPrice;
    private String description;
    private Float rating;
    private long soldQuantity;
    private long quantityAvailable;
    private Float length;
    private Float width;
    private Float breadth;
    private Float weight;
    private SellingType sellingType;
    private String SKU;
    private String brandName;
    private List<String> productImages;
    private String primaryImage;
    @Relationship(type = "HAS_DIMENSION", direction = Relationship.Direction.OUTGOING)
    private ProductDimension productDimension;
    @Relationship(type = "BELONG_TO", direction = Relationship.Direction.OUTGOING)
    private List<Category> categories;
    @Relationship(type = "HAS_REVIEW", direction = Relationship.Direction.OUTGOING)
    private List<ProductReview> reviews;
    @Relationship(type = "HAS_BANNER", direction = Relationship.Direction.OUTGOING)
    private List<ProductBanner> productBanners;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

}
