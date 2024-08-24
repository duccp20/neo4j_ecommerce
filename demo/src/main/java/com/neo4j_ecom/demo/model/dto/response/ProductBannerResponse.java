package com.neo4j_ecom.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class ProductBannerResponse {

    String id;
    String title;
    String linkUrl;
    String productName;
    List<String> bannerImages;
    String primaryBanner;
}
