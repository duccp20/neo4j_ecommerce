package com.neo4j_ecom.demo.utils.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(500, "Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NOT_FOUND(404, "Category Not Found!", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(409, "Category Already Exists!", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


}
