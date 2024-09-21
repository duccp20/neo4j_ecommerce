package com.neo4j_ecom.demo.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductBanner {

    @Id
    private String id;
    private String title;
    private String productName;
    private String primaryBanner;
    private List<String> bannerImages;
    private String linkUrl;
}
