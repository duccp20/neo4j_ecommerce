package com.neo4j_ecom.demo.utils.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import javax.tools.Diagnostic;

@Getter
public enum ErrorCode {

    //category
    UNCATEGORIZED_EXCEPTION(500, "Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NOT_FOUND(404, "Category Not Found!", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(409, "Category Already Exists!", HttpStatus.CONFLICT),
    CATEGORY_ALREADY_EXISTS_WITH_SAME_NAME(409, "Category already exists with same name", HttpStatus.CONFLICT),
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
    INVALID_FILE_SIZE(400, "Invalid File Size!", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(404, "File Not Found!", HttpStatus.NOT_FOUND),

    //Product Banner
    PRODUCT_BANNER_NOT_FOUND(404, "Product Banner Not Found!", HttpStatus.NOT_FOUND),

    WRONG_INPUT(400, "Wrong input in request!", HttpStatus.BAD_REQUEST),
    INVALID_UNIT_LENGTH(400, "Invalid Unit Length!", HttpStatus.BAD_REQUEST),
    INVALID_UNIT_PACKAGE_SIZE(400, "Invalid Unit Package Size!", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_PRICES(400, "Invalid Product Prices ", HttpStatus.BAD_REQUEST),

    //sort
    INVALID_SORT_BY(400, "Invalid Sort By!", HttpStatus.BAD_REQUEST),
    INVALID_SORT_ORDER(400, "Invalid Sort Order!", HttpStatus.BAD_REQUEST),

    //brand
    BRAND_ALREADY_EXISTS(409, "Brand Already Exists!", HttpStatus.CONFLICT),
    BRAND_NOT_FOUND(404, "Brand Not Found!", HttpStatus.NOT_FOUND),

    //Auth
    EMAIL_ALREADY_EXISTS(409, "Mail Already Exists!", HttpStatus.CONFLICT),
    LOGIN_FAILED(400, "Invalid username or password!", HttpStatus.BAD_REQUEST),


    //auth
    INVALID_REFRESH_TOKEN(401, "Invalid or expired token", HttpStatus.BAD_GATEWAY),

    REFRESH_TOKEN_NOT_FOUND(404, "Refresh Token Not Found!", HttpStatus.NOT_FOUND),
    ACCESS_DENIED(403, "Access Denied ! Do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    MISSING_REQUEST_COOKIE(400, "Missing Request Cookie!", HttpStatus.BAD_REQUEST),

    //user
    USER_NOT_FOUND(404, "User Not Found!", HttpStatus.NOT_FOUND),
    BAD_CREDENTIALS(401, "You have entered wrong credentials!", HttpStatus.UNAUTHORIZED),
    USER_NOT_VERIFIED(401, "Your account needs to be verified. Please verify now!", HttpStatus.UNAUTHORIZED),
    PRODUCT_NOT_REQUIRED_PRICE(400, "Missing Required Product Price (SALE PRICE OR ORIGINAL PRICE)!", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Invalid Password!", HttpStatus.BAD_REQUEST),

    //role
    ROLE_NOT_FOUND( 404, "Role Not Found!",  HttpStatus.NOT_FOUND );


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
