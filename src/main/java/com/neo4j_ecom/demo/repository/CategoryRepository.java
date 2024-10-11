package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.category.CategoryResponseTopSold;
import com.neo4j_ecom.demo.model.entity.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    List<Category> findByParentId(String parentId);

    @Query("{ 'parentId': ?0 }")
    List<Category> findAll();

    @Aggregation(pipeline = {
            "{ $lookup: { " +
                    "from: 'products', " +
                    "localField: '_id', " +
                    "foreignField: 'categories', " +
                    "as: 'products' " +
                    "} }",
            "{ $unwind: '$products' }",
            "{ $group: { " +
                    "_id: '$_id', " +
                    "name: { $first: '$name' }, " +
                    "totalSold: { $sum: { $ifNull: ['$products.soldQuantity', 0] } } " +
                    "} }",
            "{ $sort: { totalSold: -1 } }",
            "{ $limit: 10 }"
    })
    List<CategoryResponseTopSold> getCategoryTopSoldList();

    List<Category> findByLevel(Integer level);

    List<Category> findByIsFeatured(boolean b);

    Page<Category> findByIsFeaturedTrue(PageRequest pageRequest);
}
