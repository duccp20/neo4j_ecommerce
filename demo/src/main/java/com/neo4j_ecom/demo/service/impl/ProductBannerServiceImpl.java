package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductBanner;
import com.neo4j_ecom.demo.model.mapper.ProductBannerMapper;
import com.neo4j_ecom.demo.repository.CategoryRepository;
import com.neo4j_ecom.demo.repository.ProductBannerRepository;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.service.FileService;
import com.neo4j_ecom.demo.service.ProductBannerService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductBannerServiceImpl implements ProductBannerService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final FileService fileService;

    private final ProductBannerRepository productBannerRepository;

    private final ProductBannerMapper productBannerMapper;

    @Value("${file.image.folder.product-banner}")
    private String folder;

    @Value("${file.image.base-uri}")
    private String baseURI;

    @Override
    public ProductBanner handleCreateBanner(ProductBanner request)  {

        List<Product> products = productRepository.findAllById(request.getId());

        ProductBanner savedProductBanner = productBannerRepository.save(request);
        for (Product product : products) {
            product.getProductBanners().add(savedProductBanner);
            productRepository.save(product);
        }
        return savedProductBanner;
    }


    @Override
    public ProductBanner handleUpdateBanner(String bannerId, ProductBanner request) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        toUpdateProductBanner(productBanner, request);
        return productBannerRepository.save(productBanner);
    }

    private void toUpdateProductBanner(ProductBanner productBanner, ProductBanner request) {

        if (request.getImageUrl() != null) {
            productBanner.setImageUrl(request.getImageUrl());
        }


        if (request.getStartDate() != null) {
            productBanner.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            productBanner.setEndDate(request.getEndDate());
        }

        if (request.getLocations() != null && request.getLocations().size() > 0) {
            productBanner.setLocations(request.getLocations());
        }

    }

    @Override
    public ProductBanner handleGetBannerById(String bannerId) {
        return productBannerRepository.findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));
    }

    @Override
    public Void handleDeleteBannerById(String bannerId) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId).orElseThrow(()
                -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));
        productBannerRepository.delete(productBanner);

        return null;
    }

    @Override
    public List<ProductBanner> handleGetBannersByQuantity(int quantity) {

        log.info("quantity {}", quantity);
        List<ProductBanner> productBannerList = productBannerRepository.findAllByOrderByIdDesc(quantity);
        log.info("productBannerList {}", productBannerList);

        return productBannerList;

    }

    @Override
    public List<String> handleGetBannerImagesByQuantity(int quantity) {

        List<ProductBanner> productBannerList = productBannerRepository.findAll();

        return productBannerList.stream().map(ProductBanner::getImageUrl).collect(Collectors.toList());
    }

    @Override
    public List<ProductBanner> handleGetBanners() {
       return  productBannerRepository.findAll();
    }


}

