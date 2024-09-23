package com.neo4j_ecom.demo.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Data
@Document(collection = "refresh_tokens")
public class RefreshToken {
    @Id
    private String id;
    private User user;
    private String refreshToken;
    private Instant expiryDate;
}