package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ProductBanner;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProductBannerRepository extends Neo4jRepository<ProductBanner, String> {
}
