package com.neo4j_ecom.demo.model.dto.response.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductReviewResponse {

    private String id;
    private String content;
    private int rating;
    private String name;
    private String email;
    private String title;
    private List<ReviewOption> options;
    private String productId;
    private String reviewerId;
    private String reviewerName;
    private Instant createdAt;
    private Instant updatedAt;
}
