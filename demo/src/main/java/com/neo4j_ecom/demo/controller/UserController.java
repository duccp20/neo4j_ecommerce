package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.request.UserRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.UserResponse;
import com.neo4j_ecom.demo.service.Authentication.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    AccountService accountService;

//    @PostMapping
//    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
//        return ApiResponse.<UserResponse>builder()
//                .data(accountService.createUser(request))
//                .build();
//    }
}
