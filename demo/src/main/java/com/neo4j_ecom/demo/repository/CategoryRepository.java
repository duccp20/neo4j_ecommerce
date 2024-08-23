package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Category;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends Neo4jRepository<Category, String> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    List<Category> findByParentId(String parentId);

    @Query("MATCH (c:Category) RETURN c")
    List<Category> findAllCategories();




    @Query("MATCH (p:Product)-[:BELONG_TO]->(c:Category) " +
            "RETURN c.name, SUM(p.soldQuantity) as totalSold " +
            "ORDER BY totalSold DESC LIMIT 10")
    List<Category> findCategoriesBySoldQuantity();
}
