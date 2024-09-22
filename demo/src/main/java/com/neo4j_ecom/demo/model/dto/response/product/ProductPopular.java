package com.neo4j_ecom.demo.model.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductPopular {

    String id;
    String name;
    String image;
    String brandName;
    Float avgRating;
    Long sumSoldQuantity;
    BigDecimal discountedPrice;
    BigDecimal sellingPrice;
}
