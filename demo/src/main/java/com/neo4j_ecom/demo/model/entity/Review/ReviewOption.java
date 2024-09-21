package com.neo4j_ecom.demo.model.entity.Review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.utils.enums.ReviewType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("review_options")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewOption {

    @Id
    private String id;
    private ReviewType type;
    private Object value;


}
