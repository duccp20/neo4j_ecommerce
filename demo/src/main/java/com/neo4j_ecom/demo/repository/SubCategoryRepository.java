package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.SubCategory;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Optional;


public interface SubCategoryRepository extends Neo4jRepository<SubCategory, String> {

    Optional<SubCategory> findByName(String name);

    List<SubCategory> findByNameIn(List<String> subcategories);

    boolean existsByName(String name);
}
