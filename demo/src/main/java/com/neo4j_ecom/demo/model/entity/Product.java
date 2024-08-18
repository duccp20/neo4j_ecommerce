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
@Node
public class Product {

    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String name;
    private long price;
    private String description;
    private String image_url;
    private double rating;
    @Relationship(type = "HAS_REVIEW")
    private List<Review> reviews;
    @Relationship(type = "BELONGS_TO")
    private List<Category> categories;
    @Relationship(type = "BELONGS_TO")
    private List<SubCategory> subcategories;
    @Relationship(type = "BELONGS_TO")
    private List<ChildSubCategory> sub_subcategories;

}
