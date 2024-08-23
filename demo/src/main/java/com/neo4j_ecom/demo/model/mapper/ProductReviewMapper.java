package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;

import com.neo4j_ecom.demo.model.entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {

    ReviewResponse toResponse(ProductReview review);


    @Mapping(target = "id", ignore = true)
    ProductReview toEntity(ProductReviewRequest reviewResponse);
}
