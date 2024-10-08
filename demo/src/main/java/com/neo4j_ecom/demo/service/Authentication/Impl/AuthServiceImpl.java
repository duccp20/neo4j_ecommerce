package com.neo4j_ecom.demo.service.Authentication.Impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.Auth.InvalidateToken;
import com.neo4j_ecom.demo.model.Auth.Token;
import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.AuthRepository.InvalidateTokenRepository;
import com.neo4j_ecom.demo.service.Authentication.AuthenticationService;
import com.neo4j_ecom.demo.service.impl.UserServiceImpl;
import com.neo4j_ecom.demo.service.impl.EmailServiceImpl;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthenticationService {

    @Value("${bezkoder.app.SIGNER_KEY}")
    private String  SIGNER_KEY;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private InvalidateTokenRepository invalidateTokenRepository;

    @Autowired
    private UserServiceImpl customerService;

    @Autowired
    private EmailServiceImpl emailService;

    @Value("${domain}")
    private String domain;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User login (String email, String password) {
        Account account = accountService.findAccountByEmail(email)
                .orElseThrow(() -> new AppException (ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(password, account.getPassword())) {
            User user = customerService.getUserById(account.getUser().getId());
            user.setToken( generateToken(user));

            return customerService.saveUser(user);
        }
        throw new RuntimeException( "Invalid account or password");
    }

    @Override
    public void logout(Token token ) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token.getToken());
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        if ( expiration.before(new Date()) && signedJWT.verify(verifier)   ) {
            throw new RuntimeException( "Expired token");
        }

        InvalidateToken invalidateToken  = new InvalidateToken();
        invalidateToken.setId(signedJWT.getJWTClaimsSet().getJWTID());
        invalidateToken.setExpiryDate(expiration);

        invalidateTokenRepository.save(invalidateToken);


    }




    @Override
    public String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);


        JWTClaimsSet  jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(user.getFullName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope" ,buildScope(user))
                .build();

        Payload   payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Token verifyToken(Token token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token.getToken());
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            token.setValid(false);
            token.setExpires(null);
            token.setToken(null);
            return token;
        }

        token.setValid(signedJWT.verify(verifier)   && expirationDate.after(new Date()));
        token.setExpires(expirationDate);
        return token;


    }



    private String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> scope.add(role));
            return scope.toString();
        }
        return "";
    }


    @Override
    public Void handleForgotPassword(String email) {


        Account account = accountService.findAccountByEmail(email).orElseThrow(()
                -> new AppException (ErrorCode.USER_NOT_FOUND));

        String token = UUID.randomUUID().toString();
        accountService.updateForgotPasswordToken(token, account.getId());

        String link = domain + "/auth/forgot-password/confirmation?token=" + token + "&id=" + account.getId();

        emailService.sendMailWithLink(email, "Forgot Password", "forgotPassword", account.getFullName() , link);

        return null;
    }




    @Override
    public void verifyAccount(String email) {

        Account account = accountService.findAccountByEmail(email).orElseThrow(() ->
                new AppException (ErrorCode.USER_NOT_FOUND));

        String token = UUID.randomUUID().toString();
        accountService.updateVerificationToken(token, account.getId());

        String link = domain + "/auth/verify-account/confirmation?token=" + token + "&id=" + account.getId();

        log.info("link: {}", link);

        emailService.sendMailWithLink(email, "Confirm Registration", "confirmRegistration", account.getFullName(), link);

    }

    @Override
    public void handleResetPassword(ChangePasswordRequest request, String token) {

        Account account = accountService.findAccountByEmail(request.getEmail())
                .orElseThrow(()-> new AppException (ErrorCode.USER_NOT_FOUND));

        if (account.getForgotPasswordToken() == null || !account.getForgotPasswordToken().equals(token)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        account.setForgotPasswordToken(null);
        accountService.save(account);

    }

    @Override
    public String getCurrentUserEmail() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByEmail(email).orElseThrow(()->
                new AppException(ErrorCode.USER_NOT_FOUND));

        return account.getId();

    }

}
