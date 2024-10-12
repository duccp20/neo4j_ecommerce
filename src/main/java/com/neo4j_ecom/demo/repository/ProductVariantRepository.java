package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductVariantRepository extends MongoRepository<ProductVariant, String> {
}
