package com.neo4j_ecom.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("T3IRxlGwJRK56Cl06QycpnByk6Phkd9g1B1pUL+6JOGfMY/dxcPV28ctNv1ghGE9")
    private String  SIGNER_KEY;


    private final String[] PUBLIC_ENDPOINTS = {"/api/auth/registration", "/api/auth/login",
            "/api/auth/exists", "/api/auth/token"

    };

    private final String[] ADMIN_ENDPOINT = {

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer:: disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers( PUBLIC_ENDPOINTS).permitAll()
                                .requestMatchers( HttpMethod.POST, "/api/auth/token" ).permitAll()

                                        // For Category
                                .requestMatchers(HttpMethod.POST, "/api/v1/category/").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/category/{categoryId}/variants").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/category/{categoryId}/sub").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/category/{categoryId}/sub").hasAuthority("SCOPE_ROLE_ADMIN")

                                        // For Banners
                                .requestMatchers(HttpMethod.POST, "/api/v1/banner/multi").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/banner/multi").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/banner/{bannerId}/update").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/banner/{bannerId}/delete").hasAuthority("SCOPE_ROLE_ADMIN")


                                        // For Customer
                                .requestMatchers(HttpMethod.GET,"/api/v1/customer/all").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/v1/customer").hasAuthority("SCOPE_ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/customer/{customerId}/delete").hasAuthority("SCOPE_ROLE_ADMIN")

//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                );
        http.oauth2ResourceServer(auth2 ->
                auth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                );

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

    }

}