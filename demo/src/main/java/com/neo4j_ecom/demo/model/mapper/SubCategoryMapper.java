package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.subcategory.SubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.SubCategoryResponse;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

    @Mapping(target = "childSubCategories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    SubCategory toSubCategory(SubCategoryRequest request);

    @Mapping(target = "childSubCategories", source = "subCategory.childSubCategories")
    @Mapping(target = "categoryName", source = "subCategory.category.name")
    SubCategoryResponse toSubCategoryResponse(SubCategory subCategory);
}
