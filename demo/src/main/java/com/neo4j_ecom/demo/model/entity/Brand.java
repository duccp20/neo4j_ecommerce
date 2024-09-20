package com.neo4j_ecom.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "brands")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Brand {

    @Id
    private String id;
    private String name;
    private String description;
}
