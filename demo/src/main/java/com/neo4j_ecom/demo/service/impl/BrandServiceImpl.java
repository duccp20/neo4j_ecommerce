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
    private final UserService userService;
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

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.getUserByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Brand> brands = brandRepository.findAll();


        //brands.forEach(brand -> brand.setProducts(null));
        return brands;
    }

    @Override
    public Brand updateBrand(String id, Brand brand) {

        Brand existingBrand = brandRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.BRAND_NOT_FOUND)
        );

        boolean existedBrand = brandRepository.existsByName(brand.getName());
        if (existedBrand && !existingBrand.getName().equals(brand.getName())) {
            throw new AppException(ErrorCode.BRAND_ALREADY_EXISTS);
        }

        existingBrand.setName(brand.getName());
        existingBrand.setDescription(brand.getDescription());

//        if (brand.getProducts() != null) {
//            existingBrand.setProducts(brand.getProducts());
//        }
        return brandRepository.save(existingBrand);
    }

    @Override
    public Void deleteBrand(String id) {
        int page=0;
        int size=20;
        Brand existingBrand = this.findBrandById(id);
        if (existingBrand.getStatus().equals(Status.DELETED)){
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }

        while (true){
            PageRequest pageRequest = PageRequest.of(page,size);
            Page<Product> productPage = productRepository.findByBrandId(id,pageRequest);

            List<Product> productListToDelete = productPage.getContent();
            if (productListToDelete.isEmpty()){break;}

            for (Product product : productListToDelete){
                productService.deleteProduct(product.getId());
            }
            page++;
        }

        existingBrand.setStatus(Status.DELETED);
        brandRepository.save(existingBrand);
        return null;
    }

    @Override
    public Void revertBrand(String id) {
        int page=0;
        int size=20;
        Brand existingBrand = this.findBrandById(id);
        if (existingBrand.getStatus().equals(Status.ACTIVE)){
            throw new AppException(ErrorCode.BRAND_ALREADY_ACTIVE);
        }

        while (true){
            PageRequest pageRequest = PageRequest.of(page,size);
            Page<Product> productPage = productRepository.findByBrandId(id,pageRequest);

            List<Product> productListToRevert = productPage.getContent();
            if (productListToRevert.isEmpty()){break;}

            for (Product product : productListToRevert){
                product.setStatus(Status.ACTIVE);
            }
            productRepository.saveAll(productListToRevert);
            page++;
        }

        existingBrand.setStatus(Status.ACTIVE);
        brandRepository.save(existingBrand);
        return null;
    }

    @Override
    public Brand findBrandById(String id) {

        return brandRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.BRAND_NOT_FOUND)
        );
    }

    @Override
    public PaginationResponse getProductsByBrand(String brandId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByBrandId(brandId, pageRequest);

        List<ProductResponse> products = productPage.stream()
                .filter(p->Status.ACTIVE.equals(p.getStatus()))
                .map(productMapper::toResponse)
                .collect(Collectors.toList());

        Meta meta = Meta.builder()
                .current(productPage.getNumber()+1)
                .pageSize(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .isFirstPage(productPage.isFirst())
                .isLastPage(productPage.isLast())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .result(products)
                .build();
    }
}
