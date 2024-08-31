package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ProductDimension;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProductDimensionRepository extends Neo4jRepository<ProductDimension, String> {

}
