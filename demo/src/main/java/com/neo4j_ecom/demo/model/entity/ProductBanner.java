package com.neo4j_ecom.demo.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBanner {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String title;
    private String productName;
    private String primaryBanner;
    private List<String> bannerImages;
    private String linkUrl;
}
