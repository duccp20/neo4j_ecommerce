package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.subcategory.SubCategoryRequest;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

    SubCategory toSubCategory(SubCategoryRequest request);
}
