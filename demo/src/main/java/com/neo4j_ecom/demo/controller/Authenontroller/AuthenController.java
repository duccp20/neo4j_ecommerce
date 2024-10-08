package com.neo4j_ecom.demo.controller.Authenontroller;

import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.Auth.Token;
import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.UserResponse;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.service.Authentication.Impl.AccountServiceImpl;
import com.neo4j_ecom.demo.service.Authentication.Impl.AuthServiceImpl;
import com.neo4j_ecom.demo.service.Authentication.Impl.RefreshTokenServiceImpl;
import com.nimbusds.jose.JOSEException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@Slf4j

public class AuthenController {
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private AuthServiceImpl authenticationService;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Object> registerUser(@RequestBody() Account account){
        try{

            if (accountService.existsEmail(account.getEmail())){
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()){
                return new ResponseEntity<>("Email or password is Empty", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(accountService.registration(account), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<User> login(@RequestBody Account account){
        try {
            if (account.getPassword().isEmpty() || account.getEmail().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(authenticationService.login(account.getEmail(), account.getPassword()), HttpStatus.OK);
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @RequestMapping(value = "/logout")
    public ResponseEntity<Void> logout(@RequestBody Token token) throws ParseException, JOSEException {
        if (token == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        authenticationService.logout(token);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public ResponseEntity<Boolean> existsUser(@RequestParam String email){
        try{
            return new ResponseEntity<>(accountService.existsEmail(email), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<Token> verifyToken(@RequestBody Token token){
        try {
            return new ResponseEntity<>(authenticationService.verifyToken(token), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }





    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refresh_token") String requestRefreshToken, HttpServletResponse response) {

//        log.info("requestRefreshToken: {}", requestRefreshToken);
//
//        if (requestRefreshToken == null) {
//            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
//        }
//        RefreshToken oldRefreshToken = refreshTokenService.findByToken(requestRefreshToken);
//
//        refreshTokenService.verifyExpiration(oldRefreshToken);
//
//        this.jwtUtil.validateJwtToken(oldRefreshToken.getRefreshToken());
//
//        String accessToken = this.jwtUtil.generateTokenFromUsername(oldRefreshToken.getAccount().getEmail());
//
//        String refreshToken = this.jwtUtil.generateRefreshToken(oldRefreshToken.getAccount().getEmail());
//
//        oldRefreshToken.setRefreshToken(refreshToken);
//        refreshTokenService.saveRefreshToken(oldRefreshToken);
//
//        Cookie refreshTokenCookie = new Cookie("refresh_token", oldRefreshToken.getRefreshToken());
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
//        response.addCookie(refreshTokenCookie);
//
//        SuccessCode successCode = SuccessCode.TOKEN_REFRESH;

//
        return null;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody Map<String, String> emailMap) {

//        String email = emailMap.get("email");
//        log.info("email in forgot password: {}", email);
//        SuccessCode successCode = SuccessCode.SEND_MAIL_FORGOT_PASSWORD;
//
//        authService.handleForgotPassword(email);
//
//        return ResponseEntity.ok()
//                .body(ApiResponse.<Void>builder()
//                        .statusCode(successCode.getCode())
//                        .message(successCode.getMessage())
//                        .build()
//                );
        return null;
    }

    @GetMapping("/forgot-password/confirmation")
    public Object verifyForgotPasswordToken(@RequestParam String token, @RequestParam String id) {


//        if (token == null || id == null) {
//            return new RedirectView("http://localhost:5173/confirm-failure");
//        }
//
//        Account account = accountService.findById(id);
//
//        if (account.getForgotPasswordToken().equals(token)) {
//
//            log.info("successfully verified in forgot password token");
//
//
//            return new RedirectView("http://localhost:5173/reset?email=" + account.getEmail() + "&token=" + token);
//
//        } else {
//
//            log.info("failed verified in forgot password token");
//
//            return new RedirectView("http://localhost:5173/confirm-failure");
//        }
        return null;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> handleResetPassword(
            @RequestParam String token,
            @RequestBody ChangePasswordRequest request) {

//        SuccessCode successCode = SuccessCode.CHANGE_PASSWORD;
//
//        authService.handleResetPassword(request, token);
//        return ResponseEntity.ok()
//                .body(ApiResponse.builder()
//                        .statusCode(successCode.getCode())
//                        .message(successCode.getMessage())
//                        .build()
//                );
        return null;

    }

    @GetMapping("/account")
    public ResponseEntity<ApiResponse<UserResponse>> getAccount() {
//
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Account account = accountService.findByEmail(email);
//
//        UserResponse userResponse = UserResponse.builder()
//                .firstName(account.getFirstName())
//                .lastName(account.getLastName())
//                .email(account.getEmail())
//                .roles(account.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
//                .build();
//
//        return ResponseEntity.ok()
//                .body(ApiResponse.<UserResponse>builder()
//                        .statusCode(SuccessCode.FETCHED.getCode())
//                        .message(SuccessCode.FETCHED.getMessage())
//                        .data(userResponse)
//                        .build()
//                );

        return null;
    }

    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Object>> sendMailVerifyAccount(@RequestParam String email) {
//        authService.verifyAccount(email);
//
//        SuccessCode successCode = SuccessCode.SEND_MAIL_VERIFY_ACCOUNT;
//        return ResponseEntity.ok()
//                .body(ApiResponse.builder()
//                        .statusCode(successCode.getCode())
//                        .message(successCode.getMessage())
//                        .build()
//                );
        return null;
    }

    @GetMapping("/verify-account/confirmation")
    public Object verifyAccount(@RequestParam String token, @RequestParam String id) {

//        if (token == null || id == null) {
//            return new RedirectView("http://localhost:5173/confirm-failure");
//        }
//
//        Account account = accountService.findById(id);
//
//        if (account.getVerificationToken().equals(token)) {
//
//            account.setVerificationToken(null);
//            account.setHasVerified(true);
//
//            accountService.saveUser(account);
//            log.info("successfully verified in confirm account token");
//
//            return new RedirectView("http://localhost:5173/confirm-success");
//
//        } else {
//
//            log.info("failed verified in confirm account token");
//
//            return new RedirectView("http://localhost:5173/confirm-failure");
//        }
        return null;
    }
}
