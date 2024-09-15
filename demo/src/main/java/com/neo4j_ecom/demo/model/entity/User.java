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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("users")
public class User {

    @Id
    String id;
    String name;
    String phone;
    String email;
    String username;
    String password;
    Set<Role> role;

}
