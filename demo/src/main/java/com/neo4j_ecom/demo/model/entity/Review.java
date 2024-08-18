package com.neo4j_ecom.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Node
public class Review {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String reviewer;
    private String comment;
    private double rating;
}
