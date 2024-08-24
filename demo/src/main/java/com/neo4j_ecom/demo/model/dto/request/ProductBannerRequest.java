package com.neo4j_ecom.demo.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBannerRequest {

    private String title;
    private String primaryBanner;
    private String linkUrl;
}
