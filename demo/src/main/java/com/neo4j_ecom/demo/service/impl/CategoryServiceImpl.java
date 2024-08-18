package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import com.neo4j_ecom.demo.model.mapper.CategoryMapper;
import com.neo4j_ecom.demo.repository.CategoryRepository;
import com.neo4j_ecom.demo.repository.SubCategoryRepository;
import com.neo4j_ecom.demo.service.CategoryService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    SubCategoryRepository subCategoryRepository;

    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse handleCreateCategory(CategoryRequest request) {


        boolean existedCategory = categoryRepository.existsByName(request.getName());

        log.info("existed category: {}", existedCategory);

        if (existedCategory) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toCategory(request);

        if (request.getSubcategoriesName() != null) {
            List<SubCategory> availableSkills = this.getSubCategoriesByListName(request.getSubcategoriesName());

            category.setSubcategories(availableSkills);
        }

        Category savedCategory = categoryRepository.save(category);

        log.info("saved category: {}", savedCategory);

        return categoryMapper.toCategoryResponse(savedCategory);
    }


    @Override
    public CategoryResponse handleGetCategoryById(String id) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        log.info("category: {}", category);

        return categoryMapper.toCategoryResponse(category);

    }

    @Override
    public CategoryResponse handleUpdateCategory(String id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryMapper.updateCategory(category, request);

        if (request.getSubcategoriesName() != null) {
            List<SubCategory> availableSkills = this.getSubCategoriesByListName(request.getSubcategoriesName());
            category.setSubcategories(availableSkills);
        }

        log.info("updated category: {}", category);

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(updatedCategory);
        
    }

    @Override
    public Void handleDeleteCategory(String id) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);

        return null;
    }

    @Override
    public List<CategoryResponse> handleGetAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        log.info("categories: {}", categories);

        return categories
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    private List<SubCategory> getSubCategoriesByListName(List<String> list) {

        List<SubCategory> availableSkills = subCategoryRepository.findByNameIn(list);

        return availableSkills;
    }
    
    


}
