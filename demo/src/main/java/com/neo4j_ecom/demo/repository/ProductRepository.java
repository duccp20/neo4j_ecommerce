package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.dto.response.ProductCategoryResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);


    @Aggregation(pipeline = {
            "{ $project: { " +
                    "_id: 1, " +
                    "name: 1, " +
                    "quantityAvailable: 1, " +
                    "primaryImage: 1, " +
                    "sellingPrice: { $ifNull: ['$sellingPrice', 0] }, " +
                    "soldQuantity: { $sum: { $ifNull: ['$productVariants.soldQuantity', 0] } }, " +
                    "categories: { $map: { input: '$categories', as: 'category', in: { $ifNull: ['$category.name', ''] } } }, " +
                    "avgRating: { $avg: { $ifNull: [{ $map: { input: '$productVariants', as: 'variant', in: { $ifNull: ['$variant.avgRating', 0] } } }, []] } }, " +
                    "createdAt: 1, " +
                    "updatedAt: 1 " +
                    "}}",
            "{ $sort : { updatedAt : -1 } }"
    })
    List<ProductCategoryResponse> findProductsOrderedByLatestUpdateTime();


    @Query("{ 'productVariants._id' : ?0 }")
    Product findByVariantId(String variantId);


    Page<Product> findByCategories_Id(String categoryId, PageRequest pageRequest);

    Page<Product> findByCategories_IdAndIdNot(String categoryId, String productId, PageRequest pageRequest);
}


