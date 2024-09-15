package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.dto.response.ProductCategoryResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);



    @Aggregation(pipeline = {
            "{ $project: { id: 1, name: 1, quantityAvailable: 1, primaryImage: 1, sellingPrice: 1, createdAt: 1, updatedAt: 1, categories: { $map: { input: '$categories', as: 'category', in: '$$category.name' } } } }",
            "{ $sort: { updatedAt: -1 } }"
    })
    List<ProductCategoryResponse> findProductsOrderedByLatestUpdateTime();



//    @Query("MATCH (p:Product) RETURN p ORDER BY p.soldQuantity DESC")
//    List<Product> findProductPopularBySoldQuantity();


//    @Query("MATCH (p:Product)-[:HAS_BANNER]->(b:ProductBanner) where b.id = $bannerId RETURN p")
//    Optional<Product> findProductByBannerId(String bannerId);
}
