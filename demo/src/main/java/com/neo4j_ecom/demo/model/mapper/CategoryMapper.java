package com.neo4j_ecom.demo.model.mapper;


import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName() != null ? request.getName() : null )
                .level(request.getLevel() != null ? request.getLevel() : null)
                .icon(request.getIcon() != null ? request.getIcon() : null)
                .build();
    }

    public CategoryResponse toResponse(Category savedCategory) {


            String parent = savedCategory.getParent() != null ? savedCategory.getParent().getName() : null;


        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .level(savedCategory.getLevel())
                .icon(savedCategory.getIcon())
                .parent(parent)
                .build();
    }




}
