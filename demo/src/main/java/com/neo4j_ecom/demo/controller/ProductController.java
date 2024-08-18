package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductController {

    ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProductResponse>> handleGetAllProducts() {
        return ResponseEntity.ok().body(new ApiResponse<>());
    }
}
