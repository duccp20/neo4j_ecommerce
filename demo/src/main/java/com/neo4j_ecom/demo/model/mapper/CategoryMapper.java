package com.neo4j_ecom.demo.model.mapper;


import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.repository.CategoryRepository;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final CategoryRepository categoryRepository;

    public Category toEntity(CategoryRequest request) {

        List<Category> list = new ArrayList<>();
        if (request.getChildren() != null) {
            list.addAll(categoryRepository.findAllById(request.getChildren()));
        }

        return Category.builder()
                .name(request.getName() != null ? request.getName() : null)
                .level(request.getLevel() != null ? request.getLevel() : null)
                .icon(request.getIcon() != null ? request.getIcon() : null)
                .parent(request.getParent() != null ? categoryRepository.findById(request.getParent()).orElse(null) : null)
                .children(request.getChildren() != null ? list : null)
                .specificationOptions(request.getSpecificationOptions() != null ? request.getSpecificationOptions() : null)
                .variantOptions(request.getVariantOptions() != null ? request.getVariantOptions() : null)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .build();
    }

    public CategoryResponse toResponse(Category savedCategory) {

        String parent = savedCategory.getParent() != null ? savedCategory.getParent().getId() : null;

        return CategoryResponse.builder()
                .id(savedCategory.getId() != null ? savedCategory.getId() : null)
                .name(savedCategory.getName() != null ? savedCategory.getName() : null)
                .level(savedCategory.getLevel() != null ? savedCategory.getLevel() : null)
                .icon(savedCategory.getIcon() != null ? savedCategory.getIcon() : null)
                .parent(parent)
                .isFeatured(savedCategory.getIsFeatured() != null ? savedCategory.getIsFeatured() : false)
                .specificationOptions(savedCategory.getSpecificationOptions() != null ? savedCategory.getSpecificationOptions() : null)
                .variantOptions(savedCategory.getVariantOptions() != null ? savedCategory.getVariantOptions() : null)
                .children(savedCategory.getChildren() != null ? savedCategory.getChildren().stream().map(Category::getId).collect(Collectors.toList()) : null)
                .build();
    }

    public Category updateCategory(Category category, CategoryRequest request) {

        category.setName(request.getName() != null ? request.getName() : category.getName());
        category.setIcon(request.getIcon() != null ? request.getIcon() : category.getIcon());
        category.setParent(request.getParent() != null ? categoryRepository.findById(request.getParent()).orElse(category.getParent()) : category.getParent());
        category.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : category.getIsFeatured());
        category.setVariantOptions(request.getVariantOptions() != null ? request.getVariantOptions() : category.getVariantOptions());
        category.setSpecificationOptions(request.getSpecificationOptions() != null ? request.getSpecificationOptions() : category.getSpecificationOptions());
        category.setChildren(request.getChildren() != null ? categoryRepository.findAllById(request.getChildren()) : category.getChildren());
        category.setLevel(request.getLevel() != null ? request.getLevel() : category.getLevel());
        category.setIcon(request.getIcon() != null ? request.getIcon() : category.getIcon());

        return category;
    }



}
