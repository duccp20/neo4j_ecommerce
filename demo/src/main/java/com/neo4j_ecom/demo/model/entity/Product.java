package com.neo4j_ecom.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.math.BigDecimal;
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
    private String description;
    private double rating;
    private long soldQuantity;
    private long quantityAvailable;
    private String brandName;
    private List<String> productImages;
    private String primaryImage;
    @Relationship(type = "BELONG_TO", direction = Relationship.Direction.OUTGOING)
    private List<Category> categories;
    @Relationship(type = "HAS_REVIEW", direction = Relationship.Direction.OUTGOING)
    private List<ProductReview> reviews;
    @Relationship(type = "HAS_BANNER", direction = Relationship.Direction.OUTGOING)
    private List<ProductBanner> productBanners;

}
