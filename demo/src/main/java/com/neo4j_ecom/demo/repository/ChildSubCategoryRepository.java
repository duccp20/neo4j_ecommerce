package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ChildSubCategory;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;


public interface ChildSubCategoryRepository extends Neo4jRepository<ChildSubCategory, String> {

    Optional<ChildSubCategory> findByName(String name);
}
