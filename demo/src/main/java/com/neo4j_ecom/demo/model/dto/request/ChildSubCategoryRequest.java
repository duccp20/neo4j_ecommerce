package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.model.entity.SubCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChildSubCategoryRequest {

    String name;
    String subCategoryId;
}
