package com.neo4j_ecom.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document("product_banners")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductBanner extends BaseEntity {

    @Id
    private String id;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate  endDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> locations = new ArrayList<>();

}
