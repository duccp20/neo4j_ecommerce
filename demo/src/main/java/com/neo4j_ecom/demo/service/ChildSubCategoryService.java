package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ChildSubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ChildSubCategoryResponse;

import java.util.List;

public interface ChildSubCategoryService {
    List<ChildSubCategoryResponse> handleGetAllChildSubCategories();

    ChildSubCategoryResponse handleCreateChildSubCategory(ChildSubCategoryRequest request);

    ChildSubCategoryResponse handleUpdateChildSubCategory(String id, ChildSubCategoryRequest request);

    ChildSubCategoryResponse handleGetChildSubCategoryById(String id);

    Void handleDeleteChildSubCategory(String id);
}
