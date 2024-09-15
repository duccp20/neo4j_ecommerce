package com.neo4j_ecom.demo.model.entity.Specfication;

import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Setter
@Getter
public class ProductSpecification {
    private String description;
    private List<SpecificationOption> specificationOptions;
}
