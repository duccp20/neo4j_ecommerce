package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductPopular;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @Transactional
    public CategoryResponse handleCreateCategory(CategoryRequest request) {

        boolean existedCategory = categoryRepository.existsByName(request.getName());

        log.info("existed category: {}", existedCategory);

        if (existedCategory) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toEntity(request);
//        category.setProducts(null);

        Category savedCategory = categoryRepository.save(category);


        if (request.getParent() != null) {
            Category parent = categoryRepository.findById(request.getParent()).orElseThrow(
                    () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
            );
            parent.getChildren().add(savedCategory);
            categoryRepository.save(parent);
        }

        return categoryMapper.toResponse(category);
    }


    @Override
    public CategoryResponse handleGetCategoryById(String id) {


        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toResponse(category);

    }

    @Override
    public CategoryResponse handleUpdateCategory(String id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        boolean existedNameCategory = categoryRepository.existsByName(request.getName());
        if (existedNameCategory && !category.getName().equals(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS_WITH_SAME_NAME);
        }

        Category updatedCategory = categoryMapper.updateCategory(category, request);

        log.info("updated category: {}", updatedCategory);

        categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(updatedCategory);

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
    public List<CategoryResponse> handleGetAllCategoriesByParentId(String parentId) {

        List<Category> categories = categoryRepository.findByParentId(parentId);

        log.info("categories in handleGetAllCategoriesByParentId: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            //do not show product for this api
            categoryResponse.setProducts(Collections.emptyList());
            categoryResponses.add(categoryResponse);
        }

        return categoryResponses;
    }

    @Override
    public CategoryResponse handleGetCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        log.info("category: {}", category);

        return categoryResponse;
    }

    @Override
    public List<CategoryResponseTopSold> handleGetAllCategoriesBySoldQuantity() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponseTopSold> categoryResponseTopSoldList = new ArrayList<>();
        for (Category category : categories) {
            long sumSoldQuantity = 0;

            if (category.getIsFeatured()) {
                continue;
            }

//            for (Product product : category.getProducts()) {
//
//                if (product == null) {
//                    continue;
//                }
//
//                sumSoldQuantity += product.getSumSoldQuantity() > 0 ? product.getSumSoldQuantity() : 0;
//            }
            CategoryResponseTopSold res = CategoryResponseTopSold.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .totalSold(sumSoldQuantity)
                    .build();
            categoryResponseTopSoldList.add(res);
        }

        categoryResponseTopSoldList.sort(Comparator.comparing(CategoryResponseTopSold::getTotalSold, Comparator.reverseOrder()));

        return categoryResponseTopSoldList;
    }


    @Override
    public List<CategoryResponse> handleGetCategoriesByLevel(Integer level) {

        List<Category> categories = categoryRepository.findByLevel(level);

        log.info("categories in level: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);

            //do not show product for this api
            categoryResponse.setProducts(Collections.emptyList());
            categoryResponses.add(categoryResponse);
        }

        log.info("categoryResponses: {}", categoryResponses);

        return categoryResponses;

    }

    @Override
    public PaginationResponse handleGetProductsByCategoryId(String categoryId, Integer page, Integer size, String productId) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Product> productPage = productRepository.findByCategories_IdAndIdNot(categoryId, productId, pageRequest);

        List<ProductPopular> productResponses = productPage.getContent().stream().map(productMapper::toPopular).collect(Collectors.toList());

        Meta meta = Meta.builder()
                .current(productPage.getNumber() + 1)
                .pageSize(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .isFirstPage(productPage.isFirst())
                .isLastPage(productPage.isLast())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .result(productResponses)
                .build();
    }

    @Override
    public PaginationResponse handleGetAllCategoriesFeaturedWithProducts(Integer pageInt, Integer sizeInt) {

        PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);

        Page<Category> categoryPage = categoryRepository.findByIsFeaturedTrue(pageRequest);

        List<Category> categories = categoryPage.getContent();

//        List<CategoryResponse> categoryResponses = new ArrayList<>();
//        for (Category category : categories) {
//            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
//            categoryResponse.setProducts(category.getProducts().stream().map(productMapper::toPopular).collect(Collectors.toList()));
//            categoryResponses.add(categoryResponse);
//        }

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
//                .result(categoryResponses)
                .build();
    }

    @Override
    public List<CategoryResponse> handleGetAllCategoriesFeatured(boolean isFeatured) {

        List<Category> categories = categoryRepository.findByIsFeatured(isFeatured);

        log.info("categories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            //do not show product for this api
            categoryResponse.setProducts(Collections.emptyList());
            categoryResponses.add(categoryResponse);
        }

        log.info("categoryResponses: {}", categoryResponses);

        return categoryResponses;
    }

    @Override
    public List<CategoryResponse> handleGetAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        log.info("categories in handleGetAllCategories: {}", categories);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = categoryMapper.toResponse(category);
            categoryResponses.add(categoryResponse);
        }

        return categoryResponses;
    }


}
