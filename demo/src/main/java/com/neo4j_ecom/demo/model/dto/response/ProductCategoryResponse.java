package com.neo4j_ecom.demo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCategoryResponse {
    private String id;
    private String name;
    private Long quantityAvailable;
    private String primaryImage;
    private BigDecimal sellingPrice;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> categoriesName;


}
