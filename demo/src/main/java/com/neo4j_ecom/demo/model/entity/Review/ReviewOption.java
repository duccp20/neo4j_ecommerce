package com.neo4j_ecom.demo.model.entity.Review;

import com.neo4j_ecom.demo.utils.enums.ReviewType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
public class ReviewOption {

    private String description;
    private ReviewType type;
    private Object value;


}
