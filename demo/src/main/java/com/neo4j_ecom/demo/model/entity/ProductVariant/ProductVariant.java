package com.neo4j_ecom.demo.model.entity.ProductVariant;


import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document("product_variants")
public class ProductVariant {

    @Id
    private String id;
    private Float avgRating;
    private long quantityAvailable;
    private String SKU;
    private long soldQuantity;
    private BigDecimal sellingPrice;
    private List<String> images;
    private List<VariantOption> variantOptions;
    private List<ProductReview> reviews = new ArrayList<>();
    private int countOfReviews;
    @Transient
    private Boolean hasSpecification;

    private ProductSpecification productSpecifications;

}
