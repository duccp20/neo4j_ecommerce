package com.neo4j_ecom.demo.model.Auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Token {
    private String token;
    private boolean valid;
    private Date expires;
}
