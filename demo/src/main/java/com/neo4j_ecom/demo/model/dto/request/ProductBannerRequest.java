package com.neo4j_ecom.demo.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBannerRequest {

    @NotNull(message = "Title is required")
    private String title;
    private String primaryBanner;
    private String linkUrl;
}
