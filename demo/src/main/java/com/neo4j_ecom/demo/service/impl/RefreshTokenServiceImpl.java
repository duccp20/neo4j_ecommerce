package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.model.entity.Token;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.RefreshTokenRepository;
import com.neo4j_ecom.demo.repository.UserRepository;
import com.neo4j_ecom.demo.service.RefreshTokenService;
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
    private UserRepository userRepository;
    @Override
    public Token findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new TokenRefreshException(token, "Refresh token not found"));
    }

    @Override
    public Token createRefreshToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(jwtRefreshExpirationMs));
        token.setToken(UUID.randomUUID().toString());;
        return refreshTokenRepository.save(token);
    }
    @Override
    public void verifyExpiration(Token token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
    }

    @Override
    public void saveRefreshToken(Token oldToken) {
        refreshTokenRepository.save(oldToken);
    }
}
