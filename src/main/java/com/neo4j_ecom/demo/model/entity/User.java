package com.neo4j_ecom.demo.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"target", "source"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    String id;
    String firstName;
    String lastName;
    String phone;
    String email;
    @JsonIgnore
    private String password;
    private Set<Role> roles;
    @JsonIgnore
    boolean hasVerified;
    @JsonIgnore
    private String forgotPasswordToken;
    @JsonIgnore
    private String verificationToken;

}


