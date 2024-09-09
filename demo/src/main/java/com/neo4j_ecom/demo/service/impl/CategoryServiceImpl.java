package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.mapper.CategoryMapper;
import com.neo4j_ecom.demo.repository.CategoryRepository;
import com.neo4j_ecom.demo.service.CategoryService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse handleCreateCategory(CategoryRequest request) {

        boolean existedCategory = categoryRepository.existsByName(request.getName());

        log.info("existed category: {}", existedCategory);

        if (existedCategory) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toEntity(request);

        if (request.getParent() != null) {
            Category parent = categoryRepository.findById(request.getParent())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

            log.info("parent: {}", parent);
            category.setParent(parent);
        }

        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }

        List<Category> children = new ArrayList<>();
        if (request.getChildren() != null) {

            for (String child : request.getChildren()) {
                children.add(categoryRepository.findById(child)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
            }

            category.setChildren(children);
        }

        categoryRepository.save(category);

        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        categoryResponse.setChildren(children.stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
        log.info("created category: {}", categoryResponse);

        return categoryResponse;
    }


    @Override
    public CategoryResponse handleGetCategoryById(String id) {


        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        categoryResponse.setChildren(category.getChildren().stream().map(categoryMapper::toResponse).collect(Collectors.toList()));

        return categoryResponse;

    }

    @Override
    public CategoryResponse handleUpdateCategory(String id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        boolean existedNameCategory = categoryRepository.existsByName(request.getName());
        if (existedNameCategory && !category.getName().equals(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS_WITH_SAME_NAME);
        }

        Category updatedCategory = this.toCategoryEntityFromRequest(category, request);

        log.info("updated category: {}", updatedCategory);

        categoryRepository.save(updatedCategory);

        CategoryResponse categoryResponse = categoryMapper.toResponse(updatedCategory);
        categoryResponse.setChildren(category.getChildren().stream().map(categoryMapper::toResponse).collect(Collectors.toList()));

        return categoryResponse;

    }


    @Override
    public Void handleDeleteCategory(String id) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Category> subCategories = categoryRepository.findByParentId(id);

        if (!subCategories.isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_DELETE_CATEGORY_WITH_SUB_CATEGORIES);
        }

        categoryRepository.delete(category);

        return null;
    }

    @Override
    public List<CategoryResponse> handleGetAllCategoriesByParentId(String parentId) {

        List<Category> categories = categoryRepository.findByParentId(parentId);

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponse.setChildren(category.getChildren().stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
            categoryResponses.add(categoryResponse);
        }

        log.info("categories: {}", categories);

        return categoryResponses;
    }

    @Override
    public CategoryResponse handleGetCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        categoryResponse.setChildren(category.getChildren().stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
        log.info("category: {}", category);

        return categoryResponse;
    }

    @Override
    public List<CategoryResponseTopSold> handleGetAllCategoriesBySoldQuantity() {

//        List<CategoryResponseTopSold> categoryResponseTopSold = categoryRepository.findTopBySoldQuantity();
        return null;
    }


    @Override
    public List<CategoryResponse> handleGetCategoriesByLevel(Integer level) {

        List<Category> categories = categoryRepository.findByLevel(level);

        log.info("categories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponse.setChildren(category.getChildren().stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
            categoryResponses.add(categoryResponse);
        }

        log.info("categoryResponses: {}", categoryResponses);

        return categoryResponses;

    }

    @Override
    public List<CategoryResponse> handleGetAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        log.info("categories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponse.setChildren(category.getChildren().stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
            categoryResponses.add(categoryResponse);
        }

        log.info("categoryResponses: {}", categoryResponses);

        return categoryResponses;
    }


    //mapper
    private Category toCategoryEntityFromRequest(Category category, CategoryRequest request) {

        if (request.getName() != null) {
            category.setName(request.getName());
        }

        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }

        if (request.getParent() != null) {
            Category parent = categoryRepository.findById(request.getParent())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            category.setParent(parent);
        }

        if (request.getChildren() != null) {
            List<Category> children = new ArrayList<>();

            for (String child : request.getChildren()) {
                children.add(categoryRepository.findById(child)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
            }

            category.setChildren(children);
        }


        if (request.getLevel() != null) {
            category.setLevel(request.getLevel());
        }

        return category;
    }
}
