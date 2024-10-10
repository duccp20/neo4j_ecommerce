package com.neo4j_ecom.demo.service.impl;
import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.mapper.CategoryMapper;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.repository.CategoryRepository;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.service.CategoryService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    ProductRepository productRepository;

    ProductMapper productMapper;

    @Override
    public List<Category> handleGetAllCategoriesFeatured(boolean isFeatured) {
        return categoryRepository.findByIsFeatured(isFeatured);
    }

    @Override
    public List<Category> handleGetAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    @Override
    @Transactional
    public Category handleCreateCategory(CategoryRequest request) {

        boolean existedCategory = categoryRepository.existsByName(request.getName());

        log.info("existed category: {}", existedCategory);

        if (existedCategory) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);


        if (request.getParent() != null) {
            Category parent = categoryRepository.findById(request.getParent()).orElseThrow(
                    () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
            );
            parent.getChildren().add(savedCategory);
            categoryRepository.save(parent);
        }

        return category;
    }


    @Override
    public Category handleGetCategoryById(String id) {


        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return category;

    }

    @Override
    public Category handleUpdateCategory(String id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        boolean existedNameCategory = categoryRepository.existsByName(request.getName());
        if (existedNameCategory && !category.getName().equals(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS_WITH_SAME_NAME);
        }

        Category updatedCategory = categoryMapper.updateCategory(category, request);

        log.info("updated category: {}", updatedCategory);

        categoryRepository.save(updatedCategory);

        return updatedCategory;

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
    public List<Category> handleGetAllCategoriesByParentId(String parentId) {

        List<Category> categories = categoryRepository.findByParentId(parentId);

        log.info("categories in handleGetAllCategoriesByParentId: {}", categories);

        return categories;
    }

    @Override
    public Category handleGetCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        log.info("category: {}", category);

        return category;
    }

    @Override
    public List<CategoryResponseTopSold> handleGetAllCategoriesBySoldQuantity() {
        List<CategoryResponseTopSold> categoryResponseTopSoldList = categoryRepository.getCategoryTopSoldList();
        return categoryResponseTopSoldList;
    }

    @Override
    public List<Category> handleGetCategoriesByLevel(Integer level) {
        return categoryRepository.findByLevel(level);
    }

    @Override
    public PaginationResponse handleGetProductsByCategoryId(String categoryId, Integer pageInt, Integer sizeInt) {

            PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);

            Page<Product> productPage = productRepository.getAllProductsByCategoryId(new ObjectId(categoryId), pageRequest);

            List<Product> products = productPage.getContent();

            return PaginationResponse.builder()
                    .meta(Meta.fromPage(productPage))
                    .result(products)
                    .build();
    }

    @Override
    public PaginationResponse handleGetAllCategoriesFeaturedWithProducts(Integer pageInt, Integer sizeInt) {

        PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);

        Page<Category> categoryPage = categoryRepository.findByIsFeaturedTrue(pageRequest);

        List<Category> categories = categoryPage.getContent();

        Meta meta = Meta.builder()
                .current(categoryPage.getNumber() + 1)
                .pageSize(categoryPage.getNumberOfElements())
                .totalPages(categoryPage.getTotalPages())
                .totalItems(categoryPage.getTotalElements())
                .isFirstPage(categoryPage.isFirst())
                .isLastPage(categoryPage.isLast())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .result(categories)
                .build();
    }
}
