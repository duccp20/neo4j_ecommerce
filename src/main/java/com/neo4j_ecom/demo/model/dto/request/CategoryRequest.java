package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.utils.enums.ProductType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {

    @NotNull(message = "Category name is required")
    String name;
    String parent;
    String icon;
    Integer level;
    List<String> children;
    List<ProductType> variantOptions;
    List<ProductType> specificationOptions;
    Boolean isFeatured;

}
