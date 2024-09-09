package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ProductDimension;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductDimensionRepository extends MongoRepository<ProductDimension, String> {

}
