package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.RegisterRequest;
import com.neo4j_ecom.demo.model.dto.request.UserRequest;
import com.neo4j_ecom.demo.model.dto.response.UserResponse;
import com.neo4j_ecom.demo.model.entity.User;

public interface UserService {
    void registerUser(RegisterRequest registerRequest);
    public UserResponse createUser(UserRequest request);

    User findByEmail(String email);

    void updateForgotPasswordToken(String token, String id);

    User findById(String id);

    void updatePassword(String newPass,  User user);

    void updateVerificationToken(String token, String id);

    User saveUser(User user);
}
