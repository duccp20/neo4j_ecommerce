package com.neo4j_ecom.demo.model.entity;


import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.utils.enums.ERole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    @Id
    @Generated
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private List<String> roles = new ArrayList<>();
    private String token;


    public Customer(Account account) {
        this.fullName = account.getFullName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.roles.add(ERole.ROLE_USER.name());
        this.token="";
    }

}




