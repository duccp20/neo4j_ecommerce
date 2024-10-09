package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.entity.Brand;
import com.neo4j_ecom.demo.model.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

    Brand createBrand(Brand brand);

    List<Brand> getBrands();

    Brand updateBrand(String id, Brand brand);

    Void deleteBrand(String id);

    Void revertBrand(String id);

    Brand findBrandById(String id);

    PaginationResponse getProductsByBrand(String brandId, int page, int size);
}
