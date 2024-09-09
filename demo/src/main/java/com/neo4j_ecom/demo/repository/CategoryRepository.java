package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.entity.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    List<Category> findByParentId(String parentId);

//    List<Category> findAllCategories();
//    @Query("")
//    List<CategoryResponseTopSold> findCategoriesBySoldQuantity();
    List<Category> findByLevel(Integer level);
}
