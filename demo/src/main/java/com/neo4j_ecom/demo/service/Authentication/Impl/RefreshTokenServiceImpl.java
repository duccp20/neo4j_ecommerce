package com.neo4j_ecom.demo.service.Authentication.Impl;

import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.RefreshToken;
import com.neo4j_ecom.demo.repository.AuthRepository.AccountRepository;
import com.neo4j_ecom.demo.repository.AuthRepository.RefreshTokenRepository;
import com.neo4j_ecom.demo.service.Authentication.RefreshTokenService;
import com.neo4j_ecom.demo.utils.enums.TokenRefreshException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
@RequiredArgsConstructor
@Component
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("604800000")
    private Long jwtRefreshExpirationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token).orElseThrow(
                () -> new TokenRefreshException(token, "Refresh token not found"));
    }

    @Override
    public RefreshToken createRefreshToken(Account account) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(account);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtRefreshExpirationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());;
        return refreshTokenRepository.save(refreshToken);
    }
    @Override
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getRefreshToken(), "Refresh token was expired. Please make a new signin request");
        }
    }

    @Override
    public void saveRefreshToken(RefreshToken oldRefreshToken) {
        refreshTokenRepository.save(oldRefreshToken);
    }
}
