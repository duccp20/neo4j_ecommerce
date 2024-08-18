package com.neo4j_ecom.demo.service;


import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService{
    List<CategoryResponse> handleGetAllCategories();

    CategoryResponse handleCreateCategory(CategoryRequest request);

    CategoryResponse handleGetCategoryById(String id);

    CategoryResponse handleUpdateCategory(String id, CategoryRequest request);

    Void handleDeleteCategory(String id);
}
