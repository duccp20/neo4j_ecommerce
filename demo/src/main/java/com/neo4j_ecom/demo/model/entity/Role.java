package com.neo4j_ecom.demo.model.entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(value = "roles")
public class Role {
    @Id
    String id;
    String name;
    String description;

    Set<Permission> permissions;


}
