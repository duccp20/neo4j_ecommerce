package com.neo4j_ecom.demo.model.entity.ProductVariant;


import com.neo4j_ecom.demo.model.entity.ProductReview;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document("product_variants")
public class ProductVariant {

    @Id
    private String id;
    private Float rating;
    private long quantityAvailable;
    private String SKU;
    private long soldQuantity;
    private BigDecimal sellingPrice;
    private List<String> images;
    private List<VariantOption> variantOptions;
    private List<ProductReview> reviews;
    @Transient
    private Boolean hasSpecification;

    private ProductSpecification productSpecifications;

}
