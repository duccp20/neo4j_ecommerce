package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.entity.Token;
import com.neo4j_ecom.demo.model.entity.User;

public interface RefreshTokenService {
    public Token findByToken(String token);
    public Token createRefreshToken(User user);
    public void verifyExpiration(Token token);

    void saveRefreshToken(Token oldToken);
}
