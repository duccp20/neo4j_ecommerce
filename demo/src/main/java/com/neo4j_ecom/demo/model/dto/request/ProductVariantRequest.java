package com.neo4j_ecom.demo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.type.Decimal;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import lombok.*;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProductVariantRequest {

    private long quantityAvailable;
    private String SKU;
    private BigDecimal sellingPrice;
    private List<String> images;
    private List<VariantOption> variantOptions;

}
