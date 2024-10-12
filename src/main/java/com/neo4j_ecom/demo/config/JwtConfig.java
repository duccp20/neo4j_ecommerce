package com.neo4j_ecom.demo.config;

import com.neo4j_ecom.demo.model.entity.User;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.StringJoiner;

@Component
@Slf4j
@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String SIGNER_KEY;

    @Value("${jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public String generateToken(User user, String type){
        Instant now = Instant.now();
        Instant validity = now.plusSeconds(type.equals("access") ? accessExpiration : refreshExpiration);



        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .issuer(user.getEmail())
                .expiresAt(validity)
                .subject(user.getEmail())
                .claim("scope" , buildScope(user))
                .build();


        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        return this.jwtEncoder().encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    };


    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(SIGNER_KEY).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

    public Jwt checkValidRefreshToken(String refreshToken) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                this.getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(refreshToken);
        } catch (Exception e) {
            System.out.println(">>> JWT refresh error: " + e.getMessage());
            throw e;
        }
    }


    private String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");
        user.getRoles().forEach(role -> scope.add(role.getName()));

        log.info("scope: {}", scope.toString());
        return scope.toString();
    }
}
