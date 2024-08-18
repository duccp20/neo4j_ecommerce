package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.service.SubCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Override
    public List<CategoryResponse> handleGetAllCategories() {
        return null;
    }

    @Override
    public CategoryResponse handleCreateCategory(CategoryRequest request) {
        return null;
    }
}
