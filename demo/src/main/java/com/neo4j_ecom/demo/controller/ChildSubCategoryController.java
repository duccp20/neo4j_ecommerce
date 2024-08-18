package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.ChildSubCategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ChildSubCategoryResponse;
import com.neo4j_ecom.demo.service.ChildSubCategoryService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/childsubcategories")
@Slf4j
public class ChildSubCategoryController {

    private final ChildSubCategoryService childSubCategoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChildSubCategoryResponse>>> handleGetAllChildSubCategories() {

        SuccessCode fetchedSuccess = SuccessCode.FETCHED;
        return ResponseEntity.status(fetchedSuccess.getStatusCode()).body(
                ApiResponse.<List<ChildSubCategoryResponse>>builder()
                        .message(fetchedSuccess.getMessage())
                        .statusCode(fetchedSuccess.getCode())
                        .data(childSubCategoryService.handleGetAllChildSubCategories())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChildSubCategoryResponse>> handleCreateChildSubCategory(
            @RequestBody ChildSubCategoryRequest request
    ) {

        log.info("create child sub category request: {}", request);

        SuccessCode createdSuccessCode = SuccessCode.CREATED;
        return ResponseEntity.status(createdSuccessCode.getStatusCode()).body(
                ApiResponse.<ChildSubCategoryResponse>builder()
                        .message(createdSuccessCode.getMessage())
                        .statusCode(createdSuccessCode.getCode())
                        .data(childSubCategoryService.handleCreateChildSubCategory(request))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChildSubCategoryResponse>> handleUpdateChildSubCategory(
            @PathVariable String id,
            @RequestBody ChildSubCategoryRequest request
    ) {
        log.info("id child sub category request: {}", id);
        log.info("update child sub category request: {}", request);

        SuccessCode updatedSuccessCode = SuccessCode.UPDATED;
        return ResponseEntity.status(updatedSuccessCode.getStatusCode()).body(
                ApiResponse.<ChildSubCategoryResponse>builder()
                        .message(updatedSuccessCode.getMessage())
                        .statusCode(updatedSuccessCode.getCode())
                        .data(childSubCategoryService.handleUpdateChildSubCategory(id, request))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChildSubCategoryResponse>> handleGetChildSubCategoryById(
            @PathVariable String id
    ) {
        log.info("id child sub category request: {}", id);
        SuccessCode fetchedSuccessCode = SuccessCode.FETCHED;
        return ResponseEntity.status(fetchedSuccessCode.getStatusCode()).body(
                ApiResponse.<ChildSubCategoryResponse>builder()
                        .message(fetchedSuccessCode.getMessage())
                        .statusCode(fetchedSuccessCode.getCode())
                        .data(childSubCategoryService.handleGetChildSubCategoryById(id))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteChildSubCategory(
            @PathVariable String id
    ) {

        log.info("id child sub category request: {}", id);

        SuccessCode deletedSuccessCode = SuccessCode.DELETED;
        return ResponseEntity.status(deletedSuccessCode.getStatusCode()).body(
                ApiResponse.<Void>builder()
                        .message(deletedSuccessCode.getMessage())
                        .statusCode(deletedSuccessCode.getCode())
                        .data(childSubCategoryService.handleDeleteChildSubCategory(id))
                        .build()
        );
    }


}
