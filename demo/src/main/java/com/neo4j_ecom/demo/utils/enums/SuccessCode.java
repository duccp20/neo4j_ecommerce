package com.neo4j_ecom.demo.utils.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum SuccessCode {

    CREATED(201, "Created Successfully!", HttpStatus.CREATED),
    DELETED (200, "Deleted Successfully!", HttpStatus.OK),
    UPDATED(200, "Updated Successfully", HttpStatus.OK),
    FETCHED(200, "Get Successfully" , HttpStatus.OK), UPLOADED( 200, "Uploaded Successfully", HttpStatus.OK),
    REGISTER(200, "User Registered Successfully", HttpStatus.OK),
    LOGIN(200, "Success", HttpStatus.OK),
    LOGOUT(200, "You've been signed out!", HttpStatus.OK);
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    SuccessCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
