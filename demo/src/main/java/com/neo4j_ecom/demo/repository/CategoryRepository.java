package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
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
            "WITH c, sum(p.soldQuantity) AS totalSold " +
            "WHERE totalSold > 0 " +
            "RETURN c.id AS id, c.name AS name, totalSold " +
            "ORDER BY totalSold DESC ")
    List<CategoryResponseTopSold> findCategoriesBySoldQuantity();

    List<Category> findByLevel(Integer level);
}
