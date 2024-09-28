package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.entity.RefreshToken;
import com.neo4j_ecom.demo.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
public interface RefreshTokenService {
    public RefreshToken findByToken(String token);
    public RefreshToken createRefreshToken(User user);
    public void verifyExpiration(RefreshToken token);

    void saveRefreshToken(RefreshToken oldRefreshToken);
}
