package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.repository.BrandRepository;
import com.neo4j_ecom.demo.service.Authentication.Impl.AccountServiceImpl;
import com.neo4j_ecom.demo.service.BrandService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final AccountServiceImpl accountService;

    @Override
    public Brand handleCreateBrand(Brand brand) {

        boolean existedBrand = brandRepository.existsByName(brand.getName());
        if (existedBrand) {
            throw new AppException(ErrorCode.BRAND_ALREADY_EXISTS);
        }

        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> handleGetBrands() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountService.findAccountByEmail(email).orElseThrow(()->
                new AppException(ErrorCode.USER_NOT_FOUND));

        List<Brand> brands = brandRepository.findAll();

        brands = brands.stream().filter(brand -> brand.getExclusiveShopId() == null || brand.getExclusiveShopId().equals(account.getId())).collect(Collectors.toList());

        brands.forEach(brand -> brand.setProducts(null));
        return brands;
    }

    @Override
    public Brand handleUpdateBrand(String id, Brand brand) {

        Brand existingBrand = brandRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.BRAND_NOT_FOUND)
        );

        boolean existedBrand = brandRepository.existsByName(brand.getName());
        if (existedBrand && !existingBrand.getName().equals(brand.getName())) {
            throw new AppException(ErrorCode.BRAND_ALREADY_EXISTS);
        }

        existingBrand.setName(brand.getName());
        existingBrand.setDescription(brand.getDescription());

        if (brand.getProducts() != null) {
            existingBrand.setProducts(brand.getProducts());
        }
        return brandRepository.save(existingBrand);
    }

    @Override
    public Void handleDeleteBrand(String id) {

        brandRepository.deleteById(id);
        return null;
    }

    @Override
    public Brand handleGetBrandById(String id) {

        return brandRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.BRAND_NOT_FOUND)
        );
    }
}
