package com.neo4j_ecom.demo.repository.AuthRepository;

import com.neo4j_ecom.demo.model.Auth.InvalidateToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends MongoRepository<InvalidateToken, String> {
}
