package com.neo4j_ecom.demo.model.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.ReviewType;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseEntity {

    @Id
    private String id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private BigDecimal sellingPrice;
    private String description;
    private Float avgRating;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long quantityAvailable;
    private SellingType sellingType;
    private String sku;
    private List<String> productImages = new ArrayList<>();
    private String primaryImage;
    private long soldQuantity;
    private ProductDimension productDimension;
    private List<ProductBanner> productBanners = new ArrayList<>();
    @DocumentReference(lazy = true)
    private Brand brand;
    @DocumentReference(lazy = true)
    private List<Category> categories = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<ProductReview> reviews = new ArrayList<>();
    private ProductType primaryVariantType;
    @DocumentReference(lazy = true)
    private List<ProductVariant> productVariants;
    private List<SpecificationOption> productSpecifications;

    @DocumentReference(lazy = true)
    private List<ReviewOption> reviewOptions = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int countOfReviews;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long sumSoldQuantity;
}
