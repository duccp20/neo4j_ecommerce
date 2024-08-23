package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.service.ProductService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class ProductController {

    ProductService productService;


    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> handleCreateProduct(
            @RequestPart ProductRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ) throws URISyntaxException {

        log.info("request: {}", request);
        log.info("files: {}", files);

        SuccessCode successCode = SuccessCode.CREATED;

        return ResponseEntity.status(successCode.getCode())
                .body(
                        ApiResponse.<ProductResponse>builder()
                                .statusCode(successCode.getCode())
                                .message(successCode.getMessage())
                                .data(productService.handleCreateProduct(request, files))
                                .build()
                );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProductResponse>> handleUpdateProduct(
            @RequestParam String id,
            @RequestBody ProductRequest request
    ) {

        log.info("id product : {}", id);
        log.info("request: {}", request);

        SuccessCode successCode = SuccessCode.UPDATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<ProductResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleUpdateProduct(id, request)
                        ).build()
        );
    }


    @DeleteMapping("/{id}/images")
    public ResponseEntity<ApiResponse<Void>> handleDeleteProductImage(
            @PathVariable String id,
            @RequestParam String imgUrl
    ) {

        log.info("id product : {}", id);
        log.info("imgUrl: {}", imgUrl);


        SuccessCode successCode = SuccessCode.DELETED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<Void>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleDeleteProductImage(id ,imgUrl))
                        .build());

    };


    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<String>>> handleCreateProductImages(
            @PathVariable String productId,
            @RequestPart List<MultipartFile> files) throws URISyntaxException {

        log.info("productId: {}", productId);
        log.info("files: {}", files);

        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<List<String>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.HandleCreateProductImages(productId, files)
                        ).build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> handleGetAllProducts() {

        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.ok().body(
                ApiResponse.<List<ProductResponse>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleGetAllProducts())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> handleGetProductById(@PathVariable String id) {

        log.info("id product : {}", id);

        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.ok().body(

                ApiResponse.<ProductResponse>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleGetProductById(id))
                        .build()
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteProduct(@PathVariable String id) {
        log.info("id product : {}", id);
        SuccessCode successCode = SuccessCode.DELETED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<Void>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleDeleteProduct(id)
                        ).build()
        );
    }

    @PostMapping("/{productId}/images/primary")
    public ResponseEntity<ApiResponse<Void>> handleSetPrimaryImage(
            @PathVariable String productId,
            @RequestParam String imgUrl
    ) {

        log.info("productId: {}", productId);
        log.info("imgUrl: {}", imgUrl);

        SuccessCode successCode = SuccessCode.CREATED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<Void>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleSetPrimaryImage(productId, imgUrl)
                        ).build()
        );
    }


    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> handleGetProductPopular() {
        SuccessCode successCode = SuccessCode.FETCHED;
        return ResponseEntity.status(successCode.getCode()).body(
                ApiResponse.<List<ProductResponse>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(productService.handleGetProductPopular())
                        .build()
        );
    }


}
