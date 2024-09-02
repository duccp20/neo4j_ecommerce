package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends Neo4jRepository<Product, String> {
    boolean existsByName(String name);

    @Query("MATCH (p:Product)-[r]-(relatedNode) " +
            "WITH p, MAX(p.updatedAt) AS latestUpdate " +
            "RETURN p " +
            "ORDER BY latestUpdate DESC")
    List<Product> findProductsOrderedByLatestTime();


    @Query("MATCH (p:Product) RETURN p ORDER BY p.soldQuantity DESC")
    List<Product> findProductPopularBySoldQuantity();


    @Query("MATCH (p:Product)-[:HAS_BANNER]->(b:ProductBanner) where b.id = $bannerId RETURN p")
    Optional<Product> findProductByBannerId(String bannerId);
}
