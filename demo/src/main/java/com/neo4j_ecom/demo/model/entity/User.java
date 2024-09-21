package com.neo4j_ecom.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    String firstName;
    String lastName;
    String phone;
    String email;
    private String username;
    @JsonIgnore
    private String password;
    private Set<Role> roles;
}
