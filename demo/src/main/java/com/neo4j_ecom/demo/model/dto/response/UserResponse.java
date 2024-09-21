package com.neo4j_ecom.demo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    @JsonIgnore
    private String password;
    private Set<String> roles;

    public UserResponse(String id, String username, String email, Set<String> roles) {
        this.id = id;
        this.name = username;
        this.email = email;
        this.roles = roles;
    }
}
