package com.neo4j_ecom.demo.model.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("invalidateToken")
public class InvalidateToken {
    @Id
    private String id;
    private Date expiryDate;
}
