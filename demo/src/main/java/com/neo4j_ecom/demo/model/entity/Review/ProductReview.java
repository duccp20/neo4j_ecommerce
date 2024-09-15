package com.neo4j_ecom.demo.model.entity.Review;

import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("product_review")
public class ProductReview {

    @Id
    private String id;
    private String name;
    private String email;
    private String title;
    private int rating;
    private String content;
    List<ReviewOption> options;
    private String variantId;
    @DocumentReference
    private User reviewer;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
