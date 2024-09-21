package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import lombok.*;


import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class ProductVariantRequest {

    private long quantityAvailable;
    private String sku;
    private BigDecimal sellingPrice;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private Long soldQuantity;
    private List<String> images;
    private List<VariantOption> variantOptions;

}
