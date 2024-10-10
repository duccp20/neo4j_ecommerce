package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductBanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductBannerMapper {

<<<<<<< HEAD

    public ProductBanner toEntity(ProductBanner request) {
=======
    public ProductBanner toEntity(ProductBannerRequest request) {
>>>>>>> 89a5ff626ccdc064c14e5cf930b51a47efb00b6e

        return ProductBanner.builder()
                .imageUrl(request.getImageUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .locations(request.getLocations())
                .build();
    }

    public ProductBannerResponse toResponse(ProductBanner savedProductBanner) {

        return ProductBannerResponse.builder()
                .id(savedProductBanner.getId())
                .imageUrl(savedProductBanner.getImageUrl())
                .locations(savedProductBanner.getLocations())
                .startDate(savedProductBanner.getStartDate())
                .endDate(savedProductBanner.getEndDate())
//                .productId(savedProductBanner.getProducts().stream().map(Product::getId).collect(Collectors.toList()))
                .locations(savedProductBanner.getLocations())
//                .categoryId(savedProductBanner.getCategory().getId())
                .build();
    }
}
