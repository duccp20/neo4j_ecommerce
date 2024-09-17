package com.neo4j_ecom.demo.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    String id;
    String name;
    String phone;
    String email;
    private String username;
    private String password;
    private Set<Role> roles;
}
