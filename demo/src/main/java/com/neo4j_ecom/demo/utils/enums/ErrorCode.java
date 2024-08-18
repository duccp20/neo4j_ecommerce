package com.neo4j_ecom.demo.utils.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    //category
    UNCATEGORIZED_EXCEPTION(500, "Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NOT_FOUND(404, "Category Not Found!", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(409, "Category Already Exists!", HttpStatus.CONFLICT),

    //subcategory
    SUB_CATEGORY_ALREADY_EXISTS(409, "Sub Category Already Exists!", HttpStatus.CONFLICT),
    SUB_CATEGORY_NOT_FOUND(404, "Sub Category Not Found!", HttpStatus.NOT_FOUND),

    //product
    PRODUCT_NOT_FOUND(404, "Product Not Found!", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(409, "Product Already Exists!", HttpStatus.CONFLICT),

    //child sub category
    CHILD_SUB_CATEGORY_ALREADY_EXISTS( 409, "Child Sub Category Already Exists!", HttpStatus.CONFLICT),
    CHILD_SUB_CATEGORY_NOT_FOUND( 404, "Child Sub Category Not Found!", HttpStatus.NOT_FOUND),

    //review
    REVIEW_NOT_FOUND(404, "Review Not Found!", HttpStatus.NOT_FOUND);



    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


}
