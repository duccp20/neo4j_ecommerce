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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductBannerController {

    private final ProductBannerService productBannerService;

    @PostMapping(value = "/{productId}/banners")
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleCreateBanner(
            @PathVariable String productId,
            @Valid
            @RequestPart ProductBannerRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ) throws URISyntaxException, IOException {

        log.info("productId: {}", productId);
        log.info("request: {}", request);
        log.info("request: {}", files);


        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleCreateBanner(request, productId, files))
                        .build()
        );
    }

    @GetMapping("/{productId}/banners")
    public ResponseEntity<ApiResponse<List<ProductBannerResponse>>> handleGetBannersByProductId(@PathVariable String productId) {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<List<ProductBannerResponse>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleGetBannersByProductId(productId))
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


    @PutMapping("{productId}/banners")
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleUpdateBanner(
            @PathVariable String productId,
            @RequestParam String bannerId,
            @Valid
            @RequestPart ProductBannerRequest request,
            @RequestPart List<MultipartFile> files

    ) throws URISyntaxException, IOException {
        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleUpdateBanner(productId, bannerId, request, files))
                        .build()
        );
    }

    @PutMapping("/banners/{bannerId}/images")
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleUpdateBannerFiles(
            @PathVariable String bannerId,
            @RequestPart List<MultipartFile> files
    ) throws URISyntaxException {
        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleUpdateBannerFiles(bannerId, files))
                        .build()
        );
    }

    //update primary banner
    @PutMapping("/banners/{bannerId}/primary-image")
    public ResponseEntity<ApiResponse<ProductBannerResponse>> handleUpdateBannerPrimary(
            @PathVariable String bannerId,
            @RequestParam String url
    ) throws URISyntaxException {
        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductBannerResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleUpdateBannerPrimary(bannerId, url))
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

    @DeleteMapping("/{productId}/banners")
    public ResponseEntity<ApiResponse<Void>> handleDeleteBannerByProductId(
            @PathVariable String productId) {
        SuccessCode successCode = SuccessCode.DELETED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<Void>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleDeleteBannerByProductId(productId)
                        ).build()
        );
    }


    @DeleteMapping("/banners/{bannerId}/images")
    public ResponseEntity<ApiResponse<Void>> handleDeleteBannerImage(
            @PathVariable String bannerId,
            @RequestParam String imgUrl
    ) {
        SuccessCode successCode = SuccessCode.DELETED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<Void>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productBannerService.handleDeleteBannerImage(bannerId, imgUrl)
                        ).build()
        );
    }
}
