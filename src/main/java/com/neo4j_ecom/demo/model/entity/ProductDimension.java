package com.neo4j_ecom.demo.model.entity;


import com.neo4j_ecom.demo.utils.enums.Unit;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDimension {

    private Float length;
    private Float width;
    private Float breadth;
    private Float weight;
    private Unit unitWeight;
    private Unit packageUnit;
}
