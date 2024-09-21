package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BrandRepository extends MongoRepository<Brand, String> {
    boolean existsByName(String name);

    Brand findByName(String brandName);
}
