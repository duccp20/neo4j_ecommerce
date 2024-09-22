package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.entity.Category;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
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
    public ProductBannerResponse handleCreateBanner(ProductBannerRequest request)  {

        List<Product> products = productRepository.findAllById(request.getProductIds());

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (products.size() != request.getProductIds().size()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        ProductBanner productBanner = this.productBannerMapper.toEntity(request);
        productBanner.setProducts(products);
        productBanner.setCategory(category);
        ProductBanner savedProductBanner = productBannerRepository.save(productBanner);

        for (Product product : products) {
            product.getProductBanners().add(savedProductBanner);
            productRepository.save(product);
        }

        return this.productBannerMapper.toResponse(savedProductBanner);
    }


    @Override
    public ProductBannerResponse handleUpdateBanner(String bannerId, ProductBannerRequest request) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        ProductBanner productBannerUpdate = this.toUpdateProductBanner(productBanner, request);

        ProductBanner productBannerSaved = productBannerRepository.save(productBannerUpdate);

        for ( Product product : productBannerSaved.getProducts()) {
            List<ProductBanner> productBanners = product.getProductBanners();
            productBanners.remove(productBanner);
            product.getProductBanners().add(productBannerSaved);
            productRepository.save(product);
        }

        return this.productBannerMapper.toResponse(productBannerSaved);
    }

    private ProductBanner toUpdateProductBanner(ProductBanner productBanner, ProductBannerRequest request) {


        if (request.getProductIds() != null && request.getProductIds().size() > 0) {
            productBanner.setProducts(productRepository.findAllById(request.getProductIds()));
        }

        if (request.getImageUrl() != null) {
            productBanner.setImageUrl(request.getImageUrl());
        }

        if (request.getCategoryId() != null) {
            productBanner.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
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

        return productBanner;
    }

    @Override
    public ProductBannerResponse handleGetBannerById(String bannerId) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        return this.productBannerMapper.toResponse(productBanner);
    }

    @Override
    public Void handleDeleteBannerById(String bannerId) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        productBannerRepository.delete(productBanner);

        return null;
    }

    @Override
    public List<ProductBannerResponse> handleGetBannersByQuantity(int quantity) {

        log.info("quantity {}", quantity);
        List<ProductBanner> productBannerList = productBannerRepository.findAllByOrderByIdDesc(quantity);

        log.info("productBannerList {}", productBannerList);

        return productBannerList.stream().map(this.productBannerMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> handleGetBannerImagesByQuantity(int quantity) {

        List<ProductBanner> productBannerList = productBannerRepository.findAll();

        return productBannerList.stream().map(ProductBanner::getImageUrl).collect(Collectors.toList());
    }

    @Override
    public List<ProductBannerResponse> handleGetBanners() {

        List<ProductBanner> productBannerList = productBannerRepository.findAll();

        return productBannerList.stream().map(this.productBannerMapper::toResponse).collect(Collectors.toList());
    }


}

