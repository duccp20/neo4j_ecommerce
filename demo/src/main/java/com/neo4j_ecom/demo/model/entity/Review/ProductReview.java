package com.neo4j_ecom.demo.model.entity.Review;

import com.fasterxml.jackson.annotation.JsonInclude;


import com.neo4j_ecom.demo.model.entity.BaseEntity;

import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


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
    @Transient
    private Float avgRating;
    private String productId;
    @Transient
    private int totalReviews;

}
