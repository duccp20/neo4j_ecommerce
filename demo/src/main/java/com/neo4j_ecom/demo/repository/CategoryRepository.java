package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Category;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public class CategoryRepository extends Neo4jRepository<Category, String> {
}
