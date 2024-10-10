package com.neo4j_ecom.demo.controller;
import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.service.CategoryService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import com.neo4j_ecom.demo.utils.helper.PaginationInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> handleCreateCategory(
            @Valid
            @RequestBody CategoryRequest request) {

        log.info("create category request : {}", request);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        categoryService.handleCreateCategory(request)
                )
        );
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<Category>> handleGetCategoryById(
            @PathVariable String id) {

        log.info("get category by id request: {}", id);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetCategoryById(id)
                )
        );
    }

    @GetMapping("level/{level}")
    public ResponseEntity<ApiResponse<List<Category>>> handleGetCategoryByLevel(
            @PathVariable Integer level) {

        log.info("get category by level request: {}", level);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetCategoriesByLevel(level)
                )
        );
    }

    @PutMapping({"/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> handleUpdateCategory(
            @PathVariable String id,
            @Valid
            @RequestBody CategoryRequest request) {
        log.info("ID update category request: {}", id);
        log.info("update category request: {}", request);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        categoryService.handleUpdateCategory(id, request)
                )
        );
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<Category>>> handleGetAllCategories(
            @RequestParam boolean isFeatured) {

        log.info("get all categories request");

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetAllCategoriesFeatured(isFeatured)
                )
        );
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Category>>> handleGetAllCategories() {

        log.info("get all categories request");

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetAllCategories()
                )
        );
    }

    @GetMapping("/featured/products")
    public ResponseEntity<ApiResponse<PaginationResponse>> handleGetAllCategoriesFeaturedWithProducts(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "4") String size) {

        log.info("page: {}, size: {}", page, size);

        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetAllCategoriesFeaturedWithProducts(Integer.parseInt(page), Integer.parseInt(size))
                )
        );
    }


    @GetMapping({"/name/{name}"})
    public ResponseEntity<ApiResponse<Category>> handleGetCategoryByName(
            @PathVariable String name) {

        log.info("get category by name request: {}", name);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetCategoryByName(name)
                )
        );
    }

    @GetMapping({"/parent"})
    public ResponseEntity<ApiResponse<List<Category>>> handleGetAllCategoriesByParentId(
            @RequestParam String parentId) {

        log.info("get all categories by parent id request: {}", parentId);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetAllCategoriesByParentId(parentId)
                )
        );
    }

    @GetMapping("/top-selling")
    public ResponseEntity<ApiResponse<List<CategoryResponseTopSold>>> handleGetTopCategories() {

        log.info("get top categories request");

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetAllCategoriesBySoldQuantity()
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> handleDeleteCategory(
            @PathVariable String id) {

        log.info("delete category request: {}", id);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.DELETED,
                        categoryService.handleDeleteCategory(id)
                )
        );
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ApiResponse<PaginationResponse>> handleGetProductByCategoryId(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size,
            @PathVariable String categoryId) {

        log.info("page: {}", page);
        log.info("size: {}", size);

        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        categoryService.handleGetProductsByCategoryId(categoryId, Integer.parseInt(page), Integer.parseInt(size))
        )
        );
    }
}