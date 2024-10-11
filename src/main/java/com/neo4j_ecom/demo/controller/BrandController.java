package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.service.BrandService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import com.neo4j_ecom.demo.utils.helper.PaginationInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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
    public ResponseEntity<ApiResponse<Brand>> createBrand(
            @Valid
            @RequestBody Brand brand) {
        log.info("create brand request: {}", brand);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        brandService.createBrand(brand)
                )
        );
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<List<Brand>>> getBrands() {

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        brandService.getBrands()
                )
        );
    }

    @GetMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Brand>> getBrandById(@PathVariable String brandId) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        brandService.findBrandById(brandId)
                )
        );
    }

    @GetMapping("/{brandId}/products")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<PaginationResponse>> getProductsByBrandId(
            @PathVariable String brandId,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size) {

        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));

        return ResponseEntity.ok(ApiResponse.builderResponse(
                SuccessCode.FETCHED,
                brandService.getProductsByBrand(brandId, Integer.parseInt(page), Integer.parseInt(size))
        ));

    }

    @DeleteMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable String brandId) {

        return ResponseEntity.ok(
                ApiResponse.builderResponse(SuccessCode.DELETED, brandService.deleteBrand(brandId))
        );
    }

    @PutMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Brand>> updateBrand(@PathVariable String brandId, @Valid @RequestBody Brand brand) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        brandService.updateBrand(brandId, brand)
                ));
    }

    @PutMapping("/{brandId}/revert")
    @PreAuthorize("hasRole('ADMIN') || hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Brand>> revertBrand(@PathVariable String brandId) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        brandService.revertBrand(brandId)
                ));
    }
}
