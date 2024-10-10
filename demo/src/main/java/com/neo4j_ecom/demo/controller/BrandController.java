package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.service.BrandService;
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
@RequestMapping("api/v1/brands")
@Slf4j
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Brand>> handleCreateBrand(
            @Valid
            @RequestBody Brand brand) {
        log.info("create brand request: {}", brand);

        SuccessCode createdSuccessCode = SuccessCode.CREATED;
        return ResponseEntity.ok(
                ApiResponse.<Brand>builder()
                        .data(brandService.handleCreateBrand(brand))
                        .message(createdSuccessCode.getMessage())
                        .statusCode(createdSuccessCode.getCode())
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<List<Brand>>> handleGetBrands() {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.ok(
                ApiResponse.<List<Brand>>builder()
                        .data(brandService.handleGetBrands())
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Brand>> handleGetBrandById(@PathVariable String id) {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.ok(
                ApiResponse.<Brand>builder()
                        .data(brandService.handleGetBrandById(id))
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Brand>> handleUpdateBrand(@PathVariable String id, @Valid @RequestBody Brand brand) {
        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.ok(
                ApiResponse.<Brand>builder()
                        .data(brandService.handleUpdateBrand(id, brand))
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .build()
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteBrand(@PathVariable String id) {
        SuccessCode successCode = SuccessCode.DELETED;
        brandService.handleDeleteBrand(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message(successCode.getMessage())
                        .statusCode(successCode.getCode())
                        .build()
        );
    }


}
