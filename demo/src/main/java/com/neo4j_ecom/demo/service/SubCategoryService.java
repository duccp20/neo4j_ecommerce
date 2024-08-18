package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.subcategory.SubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.SubCategoryResponse;

import java.util.List;

public interface SubCategoryService {

    SubCategoryResponse handleCreateSubCategory(SubCategoryRequest request);

    SubCategoryResponse handleGetSubCategoryById(String id);

    List<SubCategoryResponse> handleGetAllSubCategory();

    SubCategoryResponse handleUpdateSubCategory(String id, SubCategoryRequest request);

    Void handleDeleteSubCategory(String id);
}
