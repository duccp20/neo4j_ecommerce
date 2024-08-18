package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Category;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends Neo4jRepository<Category, String> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

}
