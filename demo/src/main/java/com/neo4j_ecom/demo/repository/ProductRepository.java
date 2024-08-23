package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Neo4jRepository<Product, String> {
    boolean existsByName(String name);

    @Query("MATCH (p:Product)-[r]-(relatedNode) RETURN p, r, relatedNode")
    List<Product> findAll();


    @Query("MATCH (p:Product) RETURN p ORDER BY p.soldQuantity DESC , p.rating DESC LIMIT 20")
    List<Product> findProductPopularBySoldQuantityAndRating();
}
