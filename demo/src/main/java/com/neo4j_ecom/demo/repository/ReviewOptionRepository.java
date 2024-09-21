package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import com.neo4j_ecom.demo.utils.enums.ReviewType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewOptionRepository extends MongoRepository<ReviewOption, String> {
    ReviewOption findByType(ReviewType type);
}
