package com.neo4j_ecom.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.Status;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import java.time.Instant;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(value = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category extends BaseEntity {
    @Id
    private String id;
    private String name;
    private String icon;
    private Integer level;
    @DocumentReference(lazy = true)
    private Category parent;
    @DocumentReference(lazy = true)
    private List<Category> children;
    @DocumentReference(lazy = true)
    private List<Product> products;
    private List<ProductType> variantOptions;
    private List<ProductType> specificationOptions;
    private Boolean isFeatured;
}
