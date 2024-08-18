package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.category.CategoryRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.service.SubCategoryService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subcategories")
@Slf4j
public class SubCategoryController {

    private final SubCategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> handleCreateCategory(
            @RequestBody CategoryRequest request) {

        log.info("create category request: {}", request);

        SuccessCode createdSuccessCode = SuccessCode.CREATED;

        return ResponseEntity.status(createdSuccessCode.getStatusCode()).body(
                ApiResponse.<CategoryResponse>builder()
                        .message(createdSuccessCode.getMessage())
                        .statusCode(createdSuccessCode.getCode())
                        .data(categoryService.handleCreateCategory(request))
                        .build()
        );
    }
}
