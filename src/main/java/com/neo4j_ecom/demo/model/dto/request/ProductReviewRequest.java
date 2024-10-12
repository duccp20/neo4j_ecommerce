package com.neo4j_ecom.demo.model.dto.request;

import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {

    private String content;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not be greater than 5")
    private int rating;
    private String name;
    private String email;
    private String title;
    private List<ReviewOption> options;


}
