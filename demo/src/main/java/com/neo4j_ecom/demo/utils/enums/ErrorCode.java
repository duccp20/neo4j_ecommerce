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

    CANNOT_DELETE_CATEGORY_WITH_SUB_CATEGORIES(400, "Cannot delete category with sub-categories", HttpStatus.BAD_REQUEST),


    //product
    PRODUCT_NOT_FOUND(404, "Product Not Found!", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(409, "Product Already Exists!", HttpStatus.CONFLICT),

    //review
    REVIEW_NOT_FOUND(404, "Review Not Found!", HttpStatus.NOT_FOUND),

    //file
    MISSING_REQUIRE_PARAM(400, "Missing Require Param!", HttpStatus.BAD_REQUEST),
    INVALID_FILE_EXTENSION(400, "Invalid File Extension!", HttpStatus.BAD_REQUEST),
    INVALID_FILE_MIME_TYPE(400, "Invalid File Mime Type!", HttpStatus.BAD_REQUEST),
    INVALID_FILE_SIZE(400, "Invalid File Size!", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


    }
