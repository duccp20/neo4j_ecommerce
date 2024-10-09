package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.service.CategoryService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ApiResponse<CategoryResponse>> handleCreateCategory(
            @Valid
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

    @GetMapping("level/{level}")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> handleGetCategoryByLevel(
            @PathVariable Integer level
    ) {

        log.info("get category by level request: {}", level);

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<List<CategoryResponse>>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetCategoriesByLevel(level))
                        .build()
        );
    }

    @PutMapping({"/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> handleUpdateCategory(
            @PathVariable String id,
            @Valid
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

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> handleGetAllCategories(
            @RequestParam boolean isFeatured
    ) {

        log.info("get all categories request");

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<List<CategoryResponse>>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetAllCategoriesFeatured(isFeatured))
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> handleGetAllCategories(
    ) {

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

//    @GetMapping("/featured/products")
//    public ResponseEntity<ApiResponse<PaginationResponse>> handleGetAllCategoriesFeaturedWithProducts(
//            @RequestParam(defaultValue = "0") String page,
//            @RequestParam(defaultValue = "4") String size
//    ) {
//
//        log.info("page: {}, size: {}", page, size);
//
//        Integer pageInt = Integer.parseInt(page);
//        Integer sizeInt = Integer.parseInt(size);
//
//        try {
//            Integer.parseInt(page);
//            Integer.parseInt(size);
//        } catch (NumberFormatException e) {
//            throw new AppException(ErrorCode.WRONG_INPUT);
//        }
//
//        if (pageInt < 0 || sizeInt < 0) {
//            throw new AppException(ErrorCode.WRONG_INPUT);
//        }
//
//
//        SuccessCode successCode = SuccessCode.FETCHED;
//        return ResponseEntity.status(successCode.getCode()).body(
//                ApiResponse.<PaginationResponse>builder()
//                        .statusCode(successCode.getCode())
//                        .message(successCode.getMessage())
//                        .data(categoryService.handleGetAllCategoriesFeaturedWithProducts(pageInt, sizeInt))
//                        .build()
//        );
//    }


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
        );
    }

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

//    @GetMapping("/top-selling")
//    public ResponseEntity<ApiResponse<List<CategoryResponseTopSold>>> handleGetTopCategories() {
//        SuccessCode successCode = SuccessCode.FETCHED;
//        return ResponseEntity.status(successCode.getStatusCode()).body(
//                ApiResponse.<List<CategoryResponseTopSold>>builder()
//                        .message(successCode.getMessage())
//                        .statusCode(successCode.getCode())
//                        .data(categoryService.handleGetAllCategoriesBySoldQuantity())
//                        .build()
//        );
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ApiResponse<PaginationResponse>> handleGetProductByCategoryId(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size,
            @PathVariable String categoryId,
            @RequestParam(required = false) String productId
    ) {

        log.info("page: {}", page);
        log.info("size: {}", size);

        Integer pageInt = Integer.parseInt(page);
        Integer sizeInt = Integer.parseInt(size);

        try {
            Integer.parseInt(page);
            Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        if (pageInt < 0 || sizeInt < 0) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        SuccessCode successCode = SuccessCode.FETCHED;

        return ResponseEntity.status(successCode.getStatusCode()).body(
                ApiResponse.<PaginationResponse>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .data(categoryService.handleGetProductsByCategoryId(categoryId, pageInt, sizeInt, productId))
                        .build()
        );
    }
}
