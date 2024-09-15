package com.neo4j_ecom.demo.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;
    String name;
    String phone;
    String email;
    String userName;
    Set<String> roleNames;

    public UserResponse(Object id, String username, Object email, List<String> roles) {
    }
}
