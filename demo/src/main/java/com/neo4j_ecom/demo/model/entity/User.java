package com.neo4j_ecom.demo.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("users")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
}
