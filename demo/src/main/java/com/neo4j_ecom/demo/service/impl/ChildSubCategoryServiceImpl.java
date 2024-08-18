package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ChildSubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ChildSubCategoryResponse;
import com.neo4j_ecom.demo.model.entity.ChildSubCategory;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import com.neo4j_ecom.demo.model.mapper.ChildSubCategoryMapper;
import com.neo4j_ecom.demo.repository.ChildSubCategoryRepository;
import com.neo4j_ecom.demo.repository.SubCategoryRepository;
import com.neo4j_ecom.demo.service.ChildSubCategoryService;
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
public class ChildSubCategoryServiceImpl implements ChildSubCategoryService {

    ChildSubCategoryRepository childSubCategoryRepository;

    SubCategoryRepository subCategoryRepository;

    ChildSubCategoryMapper mapper;


    @Override
    public List<ChildSubCategoryResponse> handleGetAllChildSubCategories() {



        List<ChildSubCategory> childSubCategories = childSubCategoryRepository.findAll();

        log.info("child sub categories: {}", childSubCategories);

        return childSubCategories
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    };

    @Override
    public ChildSubCategoryResponse handleCreateChildSubCategory(ChildSubCategoryRequest request) {

        boolean existedChildSubCategory = childSubCategoryRepository.existsByName(request.getName());

        if (existedChildSubCategory) {
            throw new AppException(ErrorCode.CHILD_SUB_CATEGORY_ALREADY_EXISTS);
        }

        ChildSubCategory childSubCategory = mapper.toEntity(request);

        if (request.getSubCategoryId() != null) {

            SubCategory subCategory =  subCategoryRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.SUB_CATEGORY_NOT_FOUND));

            childSubCategory.setSubcategory(subCategory);

        } else {
            childSubCategory.setSubcategory(null);
        }

        ChildSubCategory childSubCategorySaved = childSubCategoryRepository.save(childSubCategory);

        log.info("child sub category: {}", childSubCategorySaved);

        return mapper.toResponse(childSubCategorySaved);


    }

    @Override
    public ChildSubCategoryResponse handleUpdateChildSubCategory(String id, ChildSubCategoryRequest request) {

        ChildSubCategory childSubCategory = childSubCategoryRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.CHILD_SUB_CATEGORY_NOT_FOUND));

        log.info("child sub category: {}", childSubCategory);

        mapper.updateEntity(childSubCategory, request);

        if (request.getSubCategoryId() != null) {
            SubCategory subCategory =  subCategoryRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.SUB_CATEGORY_NOT_FOUND));
            childSubCategory.setSubcategory(subCategory);
        } else {
            childSubCategory.setSubcategory(null);
        }

        ChildSubCategory childSubCategorySaved = childSubCategoryRepository.save(childSubCategory);

        log.info("child sub category: {}", childSubCategorySaved);

        return mapper.toResponse(childSubCategorySaved);
    }

    @Override
    public ChildSubCategoryResponse handleGetChildSubCategoryById(String id) {

        ChildSubCategory childSubCategory = childSubCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CHILD_SUB_CATEGORY_NOT_FOUND));

        log.info("child sub category: {}", childSubCategory);

        return mapper.toResponse(childSubCategory);

    }

    @Override
    public Void handleDeleteChildSubCategory(String id) {

        ChildSubCategory childSubCategory = childSubCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CHILD_SUB_CATEGORY_NOT_FOUND));

        childSubCategoryRepository.delete(childSubCategory);

        return null;
    }
}
