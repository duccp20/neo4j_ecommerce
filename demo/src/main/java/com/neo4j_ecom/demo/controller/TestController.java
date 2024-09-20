package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.response.ProductCategoryResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ProductRepository productRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping
    public List<ProductCategoryResponse> getProductsWithVariants() {

       return null;
    }

}
