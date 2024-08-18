package com.neo4j_ecom.demo.model.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Node(labels = "Category")
public class Category {
    @Id
    @GeneratedValue(generatorClass =
            UUIDStringGenerator.class)
    private String id;

    @Property(name = "name")
    private String name;
    @Relationship(type = "HAS_SUBCATEGORY")
    private List<SubCategory> subcategories;


}
