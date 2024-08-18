package com.neo4j_ecom.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Node(labels = "SubCategory")
public class SubCategory {

    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String name;

    @Relationship(type = "HAS_CHILD", direction = Relationship.Direction.OUTGOING)
    private List<ChildSubCategory> childSubCategories;
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Category category;
}
