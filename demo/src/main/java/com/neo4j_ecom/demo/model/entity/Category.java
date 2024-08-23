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
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;

    private String name;

    @Relationship(type = "CHILD_OF", direction = Relationship.Direction.OUTGOING)
    private Category parent;
    @Relationship(type = "CHILD_OF", direction = Relationship.Direction.INCOMING)
    private List<Category> children;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private List<Product> products;

}
