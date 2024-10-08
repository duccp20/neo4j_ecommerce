package com.neo4j_ecom.demo.model.Auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.model.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    String id;
    String fullName;
    String phone;
    String email;



    private String password;
    private Set<Role> roles;
    @JsonIgnore
    boolean hasVerified;
    @JsonIgnore
    private String forgotPasswordToken;
    @JsonIgnore
    private String verificationToken;

    @DocumentReference
    private User user;
}
