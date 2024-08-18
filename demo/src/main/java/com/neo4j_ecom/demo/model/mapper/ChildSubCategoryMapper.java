package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ChildSubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ChildSubCategoryResponse;
import com.neo4j_ecom.demo.model.entity.ChildSubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChildSubCategoryMapper {

    @Mapping(target = "subCategoryName", source = "category.name")
    ChildSubCategoryResponse toResponse(ChildSubCategory category);


    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    ChildSubCategory toEntity(ChildSubCategoryRequest request);


    void updateEntity(@MappingTarget ChildSubCategory childSubCategory, ChildSubCategoryRequest request);
}
