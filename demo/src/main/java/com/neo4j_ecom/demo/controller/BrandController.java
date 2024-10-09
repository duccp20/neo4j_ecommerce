package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.service.BrandService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
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
        return  ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        brandService.getBrands()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Brand>> getBrandById(@PathVariable String id) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        brandService.getBrandById(id)
                )
        );
    }

    @GetMapping("/{brandId}/products")
    public ResponseEntity<ApiResponse<PaginationResponse>> getProductsByBrandId(
            @PathVariable String brandId,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20")String size) {
        log.info("id brand : {}", brandId);
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

        return ResponseEntity.ok(ApiResponse.builderResponse(
                SuccessCode.FETCHED,
                brandService.getProductsByBrand(brandId, pageInt, sizeInt)
        ));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Brand>> updateBrand(@PathVariable String id, @Valid @RequestBody Brand brand) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        brandService.updateBrand(id, brand)
                ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable String id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(
                ApiResponse.builderResponse(SuccessCode.DELETED,null)
        );
    }


}
