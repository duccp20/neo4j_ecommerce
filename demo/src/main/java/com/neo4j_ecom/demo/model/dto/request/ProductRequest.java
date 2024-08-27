package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.utils.enums.SellingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotNull(message = "Product name is required")
    String name;
    String brandName;
    @NotNull(message = "Original price is required")
    @NumberFormat
    BigDecimal originalPrice;
    @NotNull(message = "Discounted price is required")
    @NumberFormat
    BigDecimal discountedPrice;
    @NotNull(message = "Description is required")
    String description;
    String SKU;

    @NotNull(message = "Weight is required")
    Float weight;

    @NotNull(message = "Length is required")
    Float length;

    @NotNull(message = "Width is required")
    Float width;

    @NotNull(message = "Breadth is required")
    Float breadth;
    @NotNull(message = "Selling type is required")
    SellingType sellingType;
    long soldQuantity;
    @NotNull(message = "Quantity available is required")
    @NumberFormat
    long quantityAvailable;
    Float rating;
    String primaryImage;
    List<String> categoryIds;

}
