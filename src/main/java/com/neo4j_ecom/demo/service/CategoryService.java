package com.neo4j_ecom.demo.service;


import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.entity.Category;


import java.util.List;

public interface CategoryService{
    List<Category> handleGetAllCategoriesFeatured(boolean isFeatured);
    List<Category> handleGetAllCategories();

    Category handleCreateCategory(CategoryRequest request);

    Category handleGetCategoryById(String id);

    Category handleUpdateCategory(String id, CategoryRequest request);

    Void handleDeleteCategory(String id);

    List<Category> handleGetAllCategoriesByParentId(String parentId);

    Category handleGetCategoryByName(String name);

    List<CategoryResponseTopSold> handleGetAllCategoriesBySoldQuantity();

    List<Category> handleGetCategoriesByLevel(Integer level);

    PaginationResponse handleGetProductsByCategoryId(String categoryId, Integer pageInt, Integer sizeInt);

    PaginationResponse handleGetAllCategoriesFeaturedWithProducts(Integer pageInt, Integer sizeInt);
}
