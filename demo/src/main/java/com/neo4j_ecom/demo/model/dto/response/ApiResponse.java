package com.neo4j_ecom.demo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.api.gax.rpc.StatusCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {

    private int statusCode;
    private Object message;
    private T data;

    public static <T> ApiResponse<T> builderResponse(SuccessCode successCode, T data) {
        return ApiResponse.<T>builder()
                .statusCode(successCode.getCode())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

}
