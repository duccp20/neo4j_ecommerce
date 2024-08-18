package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;

import java.util.List;

public interface SubCategoryService {

    List<CategoryResponse> handleGetAllCategories();

    CategoryResponse handleCreateCategory(CategoryRequest request);
}
