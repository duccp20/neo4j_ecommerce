package com.neo4j_ecom.demo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenRefreshResponse {
    private String accessToken;
}
