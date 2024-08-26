package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ProductBanner;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ProductBannerRepository extends Neo4jRepository<ProductBanner, String> {

    @Query("MATCH (p:ProductBanner) RETURN p LIMIT {quantity} DESC")
    List<ProductBanner> getBannersByQuantity(int quantity);
}
