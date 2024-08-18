package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.request.subcategory.SubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.SubCategoryResponse;
import com.neo4j_ecom.demo.service.SubCategoryService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subcategories")
@Slf4j
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubCategoryResponse>> handleCreateCategory(
            @RequestBody SubCategoryRequest request) {

        log.info("create sub category request: {}", request);

        SuccessCode createdSuccessCode = SuccessCode.CREATED;

        return ResponseEntity.status(createdSuccessCode.getStatusCode()).body(
                ApiResponse.<SubCategoryResponse>builder()
                        .message(createdSuccessCode.getMessage())
                        .statusCode(createdSuccessCode.getCode())
                        .data(subCategoryService.handleCreateSubCategory(request))
                        .build()
        );
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<SubCategoryResponse>> handleGetSubCategory(
            @PathVariable String id) {

        log.info("get sub category request: {}", id);

        SuccessCode fetchedSuccessCode = SuccessCode.FETCHED;
        return ResponseEntity.status(fetchedSuccessCode.getStatusCode()).body(
                ApiResponse.<SubCategoryResponse>builder()
                        .message(fetchedSuccessCode.getMessage())
                        .statusCode(fetchedSuccessCode.getCode())
                        .data(subCategoryService.handleGetSubCategoryById(id))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubCategoryResponse>>> handleGetAllSubCategory() {
        return ResponseEntity.status(SuccessCode.FETCHED.getStatusCode()).body(
                ApiResponse.<List<SubCategoryResponse>>builder()
                        .message(SuccessCode.FETCHED.getMessage())
                        .statusCode(SuccessCode.FETCHED.getCode())
                        .data(subCategoryService.handleGetAllSubCategory())
                        .build()
        );
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ApiResponse<SubCategoryResponse>> handleUpdateSubCategory(
            @PathVariable String id,
            @RequestBody SubCategoryRequest request
    ) {
        log.info("id sub category request: {}", id);
        log.info("payload sub category request: {}", request);

        return ResponseEntity.status(SuccessCode.UPDATED.getStatusCode()).body(
                ApiResponse.<SubCategoryResponse>builder()
                        .message(SuccessCode.UPDATED.getMessage())
                        .statusCode(SuccessCode.UPDATED.getCode())
                        .data(subCategoryService.handleUpdateSubCategory(id, request))
                        .build()
        );
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<ApiResponse<Void>> handleDeleteSubCategory(
            @PathVariable String id
    ) {

        log.info("delete sub category request: {}", id);

        SuccessCode deletedSuccessCode = SuccessCode.DELETED;
        return ResponseEntity.status(deletedSuccessCode.getStatusCode()).body(
                ApiResponse.<Void>builder()
                        .message(deletedSuccessCode.getMessage())
                        .statusCode(deletedSuccessCode.getCode())
                        .data(subCategoryService.handleDeleteSubCategory(id))
                        .build()
        );
    }
}
