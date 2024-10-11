package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductSpecificationRepository extends MongoRepository<ProductSpecification, String> {
}
