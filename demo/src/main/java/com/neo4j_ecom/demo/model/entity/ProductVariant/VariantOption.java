package com.neo4j_ecom.demo.model.entity.ProductVariant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("variant_options")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariantOption {


    private ProductType productType;
    private String valueName;
}