package com.neo4j_ecom.demo.model.entity.ProductVariant;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.type.Decimal;
import com.neo4j_ecom.demo.model.entity.ProductReview;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
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

}
