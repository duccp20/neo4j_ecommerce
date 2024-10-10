package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.service.ProductService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import com.neo4j_ecom.demo.utils.helper.PaginationInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class ProductController {

    ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Valid
            @RequestBody ProductRequest request
    ) {

        log.info("request: {}", request);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        productService.createProduct(request)
                )
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductById(@PathVariable String productId) {
        log.info("id product : {}", productId);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productService.getProductById(productId)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productService.getAllProducts()
                )
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable String productId,
            @Valid
            @RequestBody ProductRequest request
    ) {
        log.info("id product : {}", productId);
        log.info("request: {}", request);

        return ResponseEntity.status(SuccessCode.UPDATED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        productService.updateProduct(productId, request)
                )
        );
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String productId) {
        log.info("id product : {}", productId);

        return ResponseEntity.status(SuccessCode.DELETED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.DELETED,
                        productService.deleteProduct(productId)
                )
        );
    }

    @GetMapping("/top-selling")
    public ResponseEntity<ApiResponse<PaginationResponse>> getTopSelling(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "4") String size
    ) {
        log.info("page: {}", page);
        log.info("size: {}", size);

        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));

        return ResponseEntity.status(SuccessCode.FETCHED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productService.getTopProductsSold(Integer.parseInt(page), Integer.parseInt(size))
                )
        );
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> productExists(@RequestParam String name) {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productService.productExists(name)
                )
        );
    }




}