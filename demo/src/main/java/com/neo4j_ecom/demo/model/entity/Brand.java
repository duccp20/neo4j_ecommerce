package com.neo4j_ecom.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.neo4j_ecom.demo.utils.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Document(value = "brands")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Brand extends BaseEntity {

    @Id
    private String id;

    @NotBlank(message = "Brand name is required")
    @NotEmpty(message = "Brand name is not be empty")
    @NotNull(message = "Brand name is required")
    private String name;
    private String description;
//    @DocumentReference(lazy = true)
//    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
//    @JsonIgnore
//    private List<Product> products;
}