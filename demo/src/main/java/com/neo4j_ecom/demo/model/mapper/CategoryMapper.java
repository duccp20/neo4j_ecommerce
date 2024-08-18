package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryRequest request);


    @Mapping(target = "subCategories", source = "category.subcategories")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "userUpdateRequest.name")
    void updateCategory(@MappingTarget Category category, CategoryRequest userUpdateRequest);
}
