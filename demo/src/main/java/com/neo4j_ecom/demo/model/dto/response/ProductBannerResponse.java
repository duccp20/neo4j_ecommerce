package com.neo4j_ecom.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
public class ProductBannerResponse {

    private String id;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate  endDate;
    private List<String> productId;
    private List<String> locations;
    private String categoryId;
}
