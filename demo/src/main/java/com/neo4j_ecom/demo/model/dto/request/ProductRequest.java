package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.model.entity.ProductDimension;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotNull(message = "Product name is required")
    @Size(min = 5, max = 120, message = "Product name must be between 5 and 120 characters")
    String name;
    String brandName;
    @NotNull(message = "Original price is required")
    @NumberFormat
    BigDecimal originalPrice;

    @NumberFormat
    BigDecimal discountedPrice;
    @NotNull(message = "Selling price is required")
    @NumberFormat
    BigDecimal sellingPrice;
    @NotNull(message = "Description is required")
    @Size(min = 200, max = 1000, message = "Description must be between 200 and 1000 characters")
    String description;
    String SKU;
    ProductDimension productDimension;
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
