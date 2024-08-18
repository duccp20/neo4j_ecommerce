package com.neo4j_ecom.demo.model.dto.request.subcategory;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryRequest {
    String name;
    List<String> childSubCategoriesName;
    String categoryId;
}
