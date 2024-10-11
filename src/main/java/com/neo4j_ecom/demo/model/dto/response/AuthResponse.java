package com.neo4j_ecom.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@Builder
public class AuthResponse {
    private UserResponse user;
    private String token;

}
