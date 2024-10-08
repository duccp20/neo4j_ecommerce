package com.neo4j_ecom.demo.service.Authentication;

import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken findByToken(String token);
    public RefreshToken createRefreshToken(Account account);
    public void verifyExpiration(RefreshToken token);

    void saveRefreshToken(RefreshToken oldRefreshToken);
}
