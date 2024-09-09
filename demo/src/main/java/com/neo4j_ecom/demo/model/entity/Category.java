package com.neo4j_ecom.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    @Id
    private String id;
    private String name;
    private String icon;
    private Integer level;
    @DocumentReference(lazy = true)
    private Category parent;
    @DocumentReference(lazy = true)
    private List<Category> children;

//    private List<Product> products;

}
