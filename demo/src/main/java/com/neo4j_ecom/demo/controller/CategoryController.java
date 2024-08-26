package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.service.CategoryService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> handleCreateCategory(
            @RequestBody CategoryRequest request) {

        log.info("create category request : {}", request);

        SuccessCode createdSuccessCode = SuccessCode.CREATED;

        return ResponseEntity.status(createdSuccessCode.getStatusCode()).body(
                ApiResponse.<CategoryResponse>builder()
                        .message(createdSuccessCode.getMessage())
                        .statusCode(createdSuccessCode.getCode())
                        .data(categoryService.handleCreateCategory(request))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> handleGetAllCategories() {

        log.info("get all categories request");

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                        ApiResponse.<List<CategoryResponse>>builder()
                                .message(successCode.getMessage())
                                .statusCode(successCode.getCode())
                                .data(categoryService.handleGetAllCategories())
                                .build()
                );
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<CategoryResponse>> handleGetCategoryById(
            @PathVariable String id
    ) {

        log.info("get category by id request: {}", id);

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<CategoryResponse>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetCategoryById(id))
                        .build()
        );
    }

    @GetMapping({"/name/{name}"})
    public ResponseEntity<ApiResponse<CategoryResponse>> handleGetCategoryByName(
            @PathVariable String name) {

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<CategoryResponse>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetCategoryByName(name))
                        .build()
        );}

    @GetMapping({"/parent"})
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> handleGetAllCategoriesByParentId(
            @RequestParam String parentId) {

        log.info("get all categories by parent id request: {}", parentId);

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<List<CategoryResponse>>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetAllCategoriesByParentId(parentId))
                        .build()
        );
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ApiResponse<CategoryResponse>> handleUpdateCategory(
            @PathVariable String id,
            @RequestBody CategoryRequest request
    ) {
        log.info("ID update category request: {}", id);
        log.info("update category request: {}", request);

        return ResponseEntity.status(SuccessCode.UPDATED.getStatusCode()).body(
                ApiResponse.<CategoryResponse>builder()
                        .message(SuccessCode.UPDATED.getMessage())
                        .statusCode(SuccessCode.UPDATED.getCode())
                        .data(categoryService.handleUpdateCategory(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteCategory(
            @PathVariable String id
    ) {
        log.info("delete category request: {}", id);

        return ResponseEntity.status(SuccessCode.DELETED.getStatusCode()).body(
                ApiResponse.<Void>builder()
                        .message(SuccessCode.DELETED.getMessage())
                        .statusCode(SuccessCode.DELETED.getCode())
                        .data(categoryService.handleDeleteCategory(id))
                        .build()
        );
    }


    @GetMapping("/top/sold")
    public ResponseEntity<ApiResponse<List<CategoryResponseTopSold>>> handleGetTopCategories() {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<List<CategoryResponseTopSold>>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetAllCategoriesBySoldQuantity())
                        .build()
        );
    }
}
