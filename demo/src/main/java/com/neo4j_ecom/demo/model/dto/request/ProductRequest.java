package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.model.entity.ProductDimension;
import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.ReviewType;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotNull(message = "Product name is required")
    @NotBlank(message = "Product name is not be empty")
    @Size(min = 5, max = 120, message = "Product name must be between 5 and 120 characters")
    String name;
    String brandName;

    @Digits(integer = Integer.MAX_VALUE,  fraction = 2, message = "The selling price must be a valid number with up 2 decimal places")
    @NumberFormat
    BigDecimal originalPrice;

    @Digits(integer = Integer.MAX_VALUE,  fraction = 2, message = "The selling price must be a valid number with up 2 decimal places")
    BigDecimal discountedPrice;

    @NumberFormat
    @Digits(integer = Integer.MAX_VALUE,  fraction = 2, message = "The selling price must be a valid number with up 2 decimal places")
    BigDecimal sellingPrice;
    @NotNull(message = "Description is required")
    @Size(min = 200, max = 1000, message = "Description must be between 200 and 1000 characters")
    @NotBlank(message = "Description is not be empty")
    String description;
    String SKU;
    ProductDimension productDimension;
    @NotNull(message = "Selling type is required")
    SellingType sellingType;
    long soldQuantity;
    @NotNull(message = "Quantity available is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 9999, message = "Quantity must not be greater than 9999")
    long quantityAvailable;
    Float rating;
    String primaryImage;
    @NotNull(message = "Category is required")
    @NotEmpty(message = "Category is not be empty")
    List<String> categoryIds;
    Boolean hasVariants;
    Boolean hasSpecification;
    Boolean hasCollection;
    List<String> productImages;
    ProductType primaryVariantType;
    List<ProductVariantRequest> productVariants;
    List<SpecificationOption> specifications;
    List<ReviewOption> reviewOptions = new ArrayList<>();
    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public void setDescription(String desc) {
        this.description = desc != null ? desc.trim() : null;
    }
}
