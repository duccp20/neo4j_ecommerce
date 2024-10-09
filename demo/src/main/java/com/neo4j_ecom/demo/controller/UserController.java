package com.neo4j_ecom.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

//    @PostMapping
//    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
//        return ApiResponse.<UserResponse>builder()
//                .data(accountService.createUser(request))
//                .build();
//    }
}
