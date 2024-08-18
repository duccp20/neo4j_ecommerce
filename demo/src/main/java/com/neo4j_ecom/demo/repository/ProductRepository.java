package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProductRepository extends Neo4jRepository<Product, String> {
}
