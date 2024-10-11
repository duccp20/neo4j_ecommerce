package com.neo4j_ecom.demo.model.entity.Review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.entity.BaseEntity;

import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.model.entity.Product;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("product_reviews")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductReview extends BaseEntity {

    @Id
    private String id;
    private String name;
    private String email;
    private String title;
    private int rating;
    private String content;
    private List<ReviewOption> options = new ArrayList<>();
    @DocumentReference(lazy = true)
    private User reviewer;

    public static ProductReview fromRequest(ProductReviewRequest request) {
        return ProductReview.builder()
                .name(request.getName())
                .email(request.getEmail())
                .title(request.getTitle())
                .rating(request.getRating())
                .content(request.getContent())
                .options(request.getOptions())
                .build();
    }

    public User getReviewer() {
        return User.builder().id(reviewer.getId())
                .firstName(reviewer.getFirstName())
                .lastName(reviewer.getLastName())
                .build();
    }

    
    


}