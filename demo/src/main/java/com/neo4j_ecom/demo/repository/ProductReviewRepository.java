package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductReview;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ProductReviewRepository extends Neo4jRepository<ProductReview, String> {

}
