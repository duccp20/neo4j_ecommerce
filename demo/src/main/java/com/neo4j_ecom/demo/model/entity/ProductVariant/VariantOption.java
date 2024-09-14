package com.neo4j_ecom.demo.model.entity.ProductVariant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
public class VariantOption {
    private ProductType productType;
    private String valueName;
}