package com.neo4j_ecom.demo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

    private String id;
    private String name;
    private String icon;
    private Integer level;
    private String parent;
    private List<String> children;
    private List<Object> products;
    private List<ProductType> variantOptions;
    private List<ProductType> specificationOptions;
    private Boolean isFeatured;

}
