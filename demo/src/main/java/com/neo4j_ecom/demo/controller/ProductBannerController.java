package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.service.ProductBannerService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleCreateBanner(
            @Valid
            @RequestBody ProductBannerRequest request
    ) {

        log.info("request: {}", request);
        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleCreateBanner(request)
                        )
                        .build()
        );
    }

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<List<ProductBannerResponse>>> handleGetBanners() {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<List<ProductBannerResponse>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleGetBanners())
                        .build()
        );
    }

    @GetMapping("/banners/{bannerId}")
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleGetBanner(
            @PathVariable String bannerId) {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleGetBannerById(bannerId))
                        .build()
        );
    }

    @GetMapping("banners/quantity/{quantity}")
    public ResponseEntity<ApiResponse<List<ProductBannerResponse>>> handleGetBannersByQuantity(@PathVariable int quantity) {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<List<ProductBannerResponse>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleGetBannersByQuantity(quantity))
                        .build()
        );
    }

    @GetMapping("banners/images/{quantity}")
    public ResponseEntity<ApiResponse<List<String>>> handleGetBannerImagesByQuantity(@PathVariable int quantity) {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<List<String>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleGetBannerImagesByQuantity(quantity))
                        .build()
        );
    }




    @PutMapping("/banners/{bannerId}")
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleUpdateBanner(
            @PathVariable String bannerId,
            @Valid
            @RequestBody ProductBannerRequest request

    ) throws URISyntaxException, IOException {
        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleUpdateBanner(bannerId, request))
                        .build()
        );
    }


    @DeleteMapping("/banners/{bannerId}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteBanner(
            @PathVariable String bannerId) {
        SuccessCode successCode = SuccessCode.DELETED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<Void>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleDeleteBannerById(bannerId)
                        ).build()
        );
    }
}
