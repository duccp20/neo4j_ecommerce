package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.repository.BrandRepository;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.service.BrandService;
import com.neo4j_ecom.demo.service.ProductService;
import com.neo4j_ecom.demo.service.UserService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public Brand createBrand(Brand brand) {

        boolean existedBrand = brandRepository.existsByName(brand.getName());
        if (existedBrand) {
            throw new AppException(ErrorCode.BRAND_ALREADY_EXISTS);
        }

        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> getBrands() {
        return brandRepository.findAll().stream().filter(brand -> brand.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());

    }

    @Override
    public Brand updateBrand(String id, Brand brand) {

        if (brand.getStatus().equals(Status.DELETED)) {
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }

        Brand existingBrand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        boolean existedBrand = brandRepository.existsByName(brand.getName());
        if (existedBrand && !existingBrand.getName().equals(brand.getName())) {
            throw new AppException(ErrorCode.BRAND_ALREADY_EXISTS);
        }

        existingBrand.setName(brand.getName());
        existingBrand.setDescription(brand.getDescription());
        return brandRepository.save(existingBrand);
    }

    @Override
    public Void deleteBrand(String id) {
        Brand existingBrand = this.findBrandById(id);

        if (existingBrand.getStatus().equals(Status.DELETED)) {
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }

        productRepository.findByBrandId(id).forEach(product -> productService.deleteProduct(product.getId()));

        existingBrand.setStatus(Status.DELETED);
        brandRepository.save(existingBrand);

        return null;
    }

    @Override
    public Brand revertBrand(String id) {

        Brand existingBrand = this.findBrandById(id);
        if (existingBrand.getStatus().equals(Status.ACTIVE)) {
            throw new AppException(ErrorCode.BRAND_ALREADY_ACTIVE);
        }

        productRepository.findByBrandIdWithStatus(id, Status.DELETED.name())
                .forEach(product -> {
                    product.setStatus(Status.ACTIVE);
                    productRepository.save(product);
                });

        existingBrand.setStatus(Status.ACTIVE);
        return brandRepository.save(existingBrand);
    }

    @Override
    public Brand findBrandById(String id) {
        return brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    @Override
    public PaginationResponse getProductsByBrand(String brandId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByBrandId(brandId, pageRequest);

        List<Product> products = productPage.getContent().stream().filter(product -> !product.getStatus().equals(Status.DELETED)).collect(Collectors.toList());

        Meta meta = Meta.builder().current(productPage.getNumber() + 1).pageSize(productPage.getNumberOfElements()).totalPages(productPage.getTotalPages()).totalItems(productPage.getTotalElements()).isFirstPage(productPage.isFirst()).isLastPage(productPage.isLast()).build();

        return PaginationResponse.builder().meta(meta).result(products).build();
    }

}
