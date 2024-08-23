package com.neo4j_ecom.demo.model.dto.request.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    BigDecimal price;
    String description;
    List<String> categoriesId;
    List<String> subCategoriesId;
    List<String> childSubCategoriesId;

}
