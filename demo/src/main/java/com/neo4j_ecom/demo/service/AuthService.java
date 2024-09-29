package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;

public interface AuthService {
    Void handleForgotPassword(String email);

    Object verifyForgotPasswordToken(String token, String id);

    void verifyAccount(String email);

    void handleResetPassword(ChangePasswordRequest request);
}
