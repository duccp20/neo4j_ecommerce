package com.neo4j_ecom.demo.controller;

import com.google.protobuf.Api;
import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.entity.ProductBanner;
import com.neo4j_ecom.demo.service.ProductBannerService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductBannerController {

    private final ProductBannerService productBannerService;

    @PostMapping(value = "/banners")
    public ResponseEntity<ApiResponse<ProductBanner>> handleCreateBanner(
            @Valid
            @RequestBody ProductBanner request
    ) {
        log.info("request: {}", request);
//        ProductBanner createdBanner  = productBannerService.handleCreateBanner(request);
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        productBannerService.handleCreateBanner(request )
                ));
    }

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<List<ProductBanner>>> handleGetBanners() {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productBannerService.handleGetBanners()
                )
        );
    }

    @GetMapping("/banners/{bannerId}")
    public ResponseEntity<ApiResponse<ProductBanner>> handleGetBanner(
            @PathVariable String bannerId) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productBannerService.handleGetBannerById(bannerId)
                )
        );
    }

    @GetMapping("banners/quantity/{quantity}")
    public ResponseEntity<ApiResponse<List<ProductBanner>>> handleGetBannersByQuantity(@PathVariable int quantity) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productBannerService.handleGetBannersByQuantity(quantity)
                )
        );
    }

    @GetMapping("banners/images/{quantity}")
    public ResponseEntity<ApiResponse<List<String>>> handleGetBannerImagesByQuantity(@PathVariable int quantity) {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productBannerService.handleGetBannerImagesByQuantity(quantity)
                )
        );
    }


    @PutMapping("/banners/{bannerId}")
    public ResponseEntity<ApiResponse<ProductBanner>> handleUpdateBanner(
            @PathVariable String bannerId,
            @Valid
            @RequestBody ProductBanner request

    ) throws URISyntaxException, IOException {
        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        productBannerService.handleUpdateBanner(bannerId, request)
                )
        );
    }


    @DeleteMapping("/banners/{bannerId}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteBanner(
            @PathVariable String bannerId) {
        SuccessCode successCode = SuccessCode.DELETED;
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.DELETED,
                        productBannerService.handleDeleteBannerById(bannerId)
                )
        );
    }
}
