package com.neo4j_ecom.demo.repository.AuthRepository;


import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String token);

//    int deleteByUser(Account account);
}