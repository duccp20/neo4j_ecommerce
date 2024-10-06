package com.neo4j_ecom.demo.repository;


import com.neo4j_ecom.demo.model.entity.Token;
import com.neo4j_ecom.demo.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByToken(String token);

    int deleteByUser(User user);
}