package com.neo4j_ecom.demo.model.entity;


import com.neo4j_ecom.demo.utils.enums.Unit;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDimension {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private Float length;
    private Float width;
    private Float breadth;
    private Float weight;
    private Unit unitWeight;
    private Unit packageUnit;
}
