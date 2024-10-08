package com.neo4j_ecom.demo.service.Authentication;

import com.neo4j_ecom.demo.model.Auth.Token;
import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    public User login(String email, String password);
    public void logout(Token token) throws JOSEException, ParseException;
    public String generateToken(User userName);
    public Token verifyToken(Token token) throws JOSEException, ParseException;



    Void handleForgotPassword(String email);

    void verifyAccount(String email);

    void handleResetPassword(ChangePasswordRequest request, String token);
    String getCurrentUserEmail();

}
