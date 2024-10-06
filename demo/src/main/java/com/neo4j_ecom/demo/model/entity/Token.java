package com.neo4j_ecom.demo.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Getter
@Setter
@Data
@Document(collection = "tokens")
public class Token {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private User user;
    private String token;
    private Instant expiryDate;
}