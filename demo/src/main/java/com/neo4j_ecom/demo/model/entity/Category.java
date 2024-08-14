package com.neo4j_ecom.demo.model.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Node
public class Category {

    @Id
    @GeneratedValue()
    private Long id;
    private String name;

}
