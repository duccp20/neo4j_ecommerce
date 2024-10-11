package com.neo4j_ecom.demo.model.dto.response.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {

    int current;
    int pageSize;
    int totalPages;
    long totalItems;
    boolean isLastPage;
    boolean isFirstPage;

    public static <T> Meta fromPage(Page<T> page) {
        return Meta.builder()
                .current(page.getNumber() + 1)
                .pageSize(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .build();
    }
}
