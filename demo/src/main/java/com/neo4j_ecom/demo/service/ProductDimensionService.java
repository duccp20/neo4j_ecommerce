package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.entity.ProductDimension;

public interface ProductDimensionService {

    ProductDimension createProductDimension(ProductDimension request);
    ProductDimension updateProductDimension(ProductDimension current, ProductDimension request);
}
