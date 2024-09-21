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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse handleCreateCategory(CategoryRequest request) {

        boolean existedCategory = categoryRepository.existsByName(request.getName());

        log.info("existed category: {}", existedCategory);

        if (existedCategory) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toEntity(request);
        category.setProducts(null);

        Category savedCategory = categoryRepository.save(category);


        if (request.getParent() != null) {
            Category parent = categoryRepository.findById(request.getParent()).orElseThrow(
                    () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
            );
            parent.getChildren().add(savedCategory);
            categoryRepository.save(parent);
        }

        return categoryMapper.toResponse(category);
    }


    @Override
    public CategoryResponse handleGetCategoryById(String id) {


        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toResponse(category);

    }

    @Override
    public CategoryResponse handleUpdateCategory(String id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        boolean existedNameCategory = categoryRepository.existsByName(request.getName());
        if (existedNameCategory && !category.getName().equals(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS_WITH_SAME_NAME);
        }

        Category updatedCategory = categoryMapper.updateCategory(category, request);

        log.info("updated category: {}", updatedCategory);

        categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(updatedCategory);

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

        log.info("categories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponses.add(categoryResponse);
        }

        return categoryResponses;
    }

    @Override
    public CategoryResponse handleGetCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
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

        log.info("categories in level: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponses.add(categoryResponse);
        }

        log.info("categoryResponses: {}", categoryResponses);

        return categoryResponses;

    }

    @Override
    public List<CategoryResponse> handleGetAllCategoriesFeatured(boolean isFeatured) {

        List<Category> categories = categoryRepository.findByIsFeatured(isFeatured);

        log.info("categories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponses.add(categoryResponse);
        }

        log.info("categoryResponses: {}", categoryResponses);

        return categoryResponses;
    }

    @Override
    public List<CategoryResponse> handleGetAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        log.info("categories in handleGetAllCategories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponses.add(categoryResponse);
        }

        return categoryResponses;
    }


}
