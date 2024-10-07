package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.service.ProductService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
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
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        productService.getAllProducts()
                )
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @RequestParam String id,
            @Valid
            @RequestBody ProductRequest request
    ) {
        log.info("id product : {}", id);
        log.info("request: {}", request);

        return ResponseEntity.status(SuccessCode.UPDATED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        productService.updateProduct(id, request)
                )
        );
    }


    @DeleteMapping("/{id}/images")
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @PathVariable String id,
            @RequestParam String imgUrl
    ) {
        log.info("id product : {}", id);
        log.info("imgUrl: {}", imgUrl);

        return ResponseEntity.status(SuccessCode.DELETED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.DELETED,
                        productService.deleteProductImage(id, imgUrl)
                )
        );
    }


    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<String>>> createProductImages(
            @PathVariable String productId,
            @RequestPart List<MultipartFile> files
    ) throws URISyntaxException, IOException {
        log.info("productId: {}", productId);
        log.info("files: {}", files);

        return ResponseEntity.status(SuccessCode.CREATED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        productService.createProductImages(productId, files)
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




    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        log.info("id product : {}", id);

        return ResponseEntity.status(SuccessCode.DELETED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.DELETED,
                        productService.deleteProduct(id)
                )
        );
    }

    @PostMapping("/{productId}/images/primary-image")
    public ResponseEntity<ApiResponse<Void>> setPrimaryImage(
            @PathVariable String productId,
            @RequestParam String imgUrl
    ) {
        log.info("productId: {}", productId);
        log.info("imgUrl: {}", imgUrl);

        return ResponseEntity.status(SuccessCode.CREATED.getCode()).body(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        productService.setPrimaryImage(productId, imgUrl)
                )
        );
    }


//    @GetMapping("/top-selling")
//    public ResponseEntity<ApiResponse<PaginationResponse>> getTopSelling(
//            @RequestParam(defaultValue = "0") String page,
//            @RequestParam(defaultValue = "4") String size
//    ) {
//        log.info("page: {}", page);
//        log.info("size: {}", size);
//
//        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));
//
//        return ResponseEntity.status(SuccessCode.FETCHED.getCode()).body(
//                ApiResponse.builderResponse(
//                        SuccessCode.FETCHED,
//                        productService.getTopSelling(Integer.parseInt(page), Integer.parseInt(size))
//                )
//        );
//    }
}