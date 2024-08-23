package com.neo4j_ecom.demo.model.dto.request;

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
    String brandName;
    BigDecimal originalPrice;
    BigDecimal discountedPrice;
    String description;
    long soldQuantity;
    long quantityAvailable;
    double rating;
    String primaryImage;
    List<String> categoryIds;

}
