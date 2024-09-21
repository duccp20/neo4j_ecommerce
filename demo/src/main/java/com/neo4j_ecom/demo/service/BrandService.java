package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.entity.Brand;

import java.util.List;

public interface BrandService {

    Brand handleCreateBrand(Brand brand);

    List<Brand> handleGetBrands();

    Brand handleUpdateBrand(String id, Brand brand);

    Void handleDeleteBrand(String id);

    Brand handleGetBrandById(String id);

}
