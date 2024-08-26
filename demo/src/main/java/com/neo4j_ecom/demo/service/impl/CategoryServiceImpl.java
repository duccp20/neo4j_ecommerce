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

        Category category = categoryMapper.toCategory(request);

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
        if (request.getChildrens() != null) {

            for (String child : request.getChildrens()) {
                children.add(categoryRepository.findById(child)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
            }

            category.setChildren(children);
        }

        categoryRepository.save(category);

        //response

        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
        if (children.size() > 0) {
            categoryResponse.setChildrens(this.toChildrensCategoryResponses(children));
        }

        return categoryResponse;
    }

    private List<CategoryResponse> toChildrensCategoryResponses(List<Category> children) {

       List<CategoryResponse> res = new ArrayList<>();

       for (Category child : children) {
           res.add(categoryMapper.toCategoryResponse(child));
       }

       return res;


    }


    @Override
    public CategoryResponse handleGetCategoryById(String id) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse res = categoryMapper.toCategoryResponse(category);
        if (category.getChildren().size() > 0) {
            res.setChildrens(this.toChildrensCategoryResponses(category.getChildren()));
        }

        return res;

    }

    @Override
    public CategoryResponse handleUpdateCategory(String id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryMapper.updateCategory(category, request);

        log.info("updated category: {}", category);

        if (request.getParent() != null) {
            Category parent = categoryRepository.findById(request.getParent())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            category.setParent(parent);
        }

        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }

        if (request.getChildrens() != null) {
            List<Category> children = new ArrayList<>();

            for (String child : request.getChildrens()) {
                children.add(categoryRepository.findById(child)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
            }

            category.setChildren(children);
        }

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(updatedCategory);

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

        return categories
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse handleGetCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
        if (category.getChildren().size() > 0) {
            categoryResponse.setChildrens(this.toChildrensCategoryResponses(category.getChildren()));
        }

        return categoryResponse;
    }

    @Override
    public List<CategoryResponseTopSold> handleGetAllCategoriesBySoldQuantity() {

        List<CategoryResponseTopSold> categories = categoryRepository.findCategoriesBySoldQuantity();

        log.info("categories: {}", categories);

        return categories;

    }

    @Override
    public List<CategoryResponse> handleGetAllCategories() {

//        List<Category> categories = categoryRepository.findAllCategories();

        List<Category> categories = categoryRepository.findAll();

        log.info("categories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {

            CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
            if (category.getChildren().size() > 0) {
                categoryResponse.setChildrens(this.toChildrensCategoryResponses(category.getChildren()));
            }

            categoryResponses.add(categoryResponse);
        }

        return categoryResponses;
    }


}
