package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {



    ReviewResponse toResponse(Review review);


    @Mapping(target = "id", ignore = true)
    Review toEntity(ReviewRequest reviewResponse);
}
