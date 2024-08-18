package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.request.subcategory.SubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.SubCategoryResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.ChildSubCategory;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import com.neo4j_ecom.demo.model.mapper.SubCategoryMapper;
import com.neo4j_ecom.demo.repository.CategoryRepository;
import com.neo4j_ecom.demo.repository.ChildSubCategoryRepository;
import com.neo4j_ecom.demo.repository.SubCategoryRepository;
import com.neo4j_ecom.demo.service.SubCategoryService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SubCategoryServiceImpl implements SubCategoryService {

    SubCategoryRepository subCategoryRepository;

    CategoryRepository categoryRepository;

    ChildSubCategoryRepository childSubCategoryRepository;

    SubCategoryMapper subCategoryMapper;


    @Override
    public SubCategoryResponse handleCreateSubCategory(SubCategoryRequest request) {

        boolean existedSubCategory = subCategoryRepository.existsByName(request.getName());

        if (existedSubCategory) {
            throw new AppException(ErrorCode.SUB_CATEGORY_ALREADY_EXISTS);
        }

        SubCategory subCategory = subCategoryMapper.toSubCategory(request);

        if (request.getCategoryId() != null) {

            Category category = this.checkExistedCompany(request);

            subCategory.setCategory(category);
        }

        if (request.getChildSubCategoriesName() != null) {
            List<ChildSubCategory> subCategories = childSubCategoryRepository.findByNameIn(request.getChildSubCategoriesName());
            subCategory.setChildSubCategories(subCategories);
        }


        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);

        log.info("saved sub category: {}", savedSubCategory);

        return subCategoryMapper.toSubCategoryResponse(savedSubCategory);
    }



    @Override
    public SubCategoryResponse handleGetSubCategoryById(String id) {

        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUB_CATEGORY_NOT_FOUND));

        log.info("get sub category: {}", subCategory);

        return subCategoryMapper.toSubCategoryResponse(subCategory);

    }

    @Override
    public List<SubCategoryResponse> handleGetAllSubCategory() {

        List<SubCategory> subCategories = subCategoryRepository.findAll();

        log.info("get all sub categories: {}", subCategories);

        return subCategories.stream()
                .map(subCategoryMapper::toSubCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubCategoryResponse handleUpdateSubCategory(String id, SubCategoryRequest request) {
        
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SUB_CATEGORY_NOT_FOUND)
        );
        
        if (request.getName() != null) {
            subCategory.setName(request.getName());
        }
        
        if (request.getCategoryId() != null) {
            Category category = this.checkExistedCompany(request);
            subCategory.setCategory(category);
        } else {
            subCategory.setCategory(null);
        }
        
        if (request.getChildSubCategoriesName() != null) {
            List<ChildSubCategory> subCategories = childSubCategoryRepository.findByNameIn(request.getChildSubCategoriesName());
            subCategory.setChildSubCategories(subCategories);
        } else {
            subCategory.setChildSubCategories(null);
        }
        
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);

        return subCategoryMapper.toSubCategoryResponse(updatedSubCategory);
        
    }


    private Category checkExistedCompany(SubCategoryRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return category;
    }

    @Override
    public Void handleDeleteSubCategory(String id) {

        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SUB_CATEGORY_NOT_FOUND)
        );

        subCategoryRepository.delete(subCategory);

        return null;
    }

}
