package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.config.JwtConfig;
import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.dto.request.RegisterRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.service.AuthService;
import com.neo4j_ecom.demo.service.UserService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final UserService userService;

    private final JwtConfig jwtConfig;


    @Value("${jwt.refresh-expiration}")
    private long maxAge;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(ApiResponse.builderResponse(SuccessCode.REGISTER, authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody Map<String, String> request) {

        Map<String, Object> response = authService.login(request);

        User user = (User) response.get("user");
        String refreshToken = jwtConfig.generateToken(user, "refresh");
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .build();

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, responseCookie.toString()).
                body(ApiResponse.builderResponse(SuccessCode.LOGIN, authService.login(request)));

    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("refresh_token") String refreshToken,  HttpServletResponse response) {

        if (refreshToken == null) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.
                ok(ApiResponse.builderResponse(SuccessCode.LOGOUT, null));

    }



    @GetMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@CookieValue("refresh_token") String requestRefreshToken) {

        Jwt decodedOldRefreshToken = this.jwtConfig.checkValidRefreshToken(requestRefreshToken);

        String email = decodedOldRefreshToken.getSubject();

        if (email == null) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = this.jwtConfig.generateToken(user, "access");

        return ResponseEntity.ok()
                .body(ApiResponse.builderResponse(SuccessCode.CREATED, newAccessToken));

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody Map<String, String> emailMap) {

        String email = emailMap.get("email");
        log.info("email in forgot password: {}", email);
        SuccessCode successCode = SuccessCode.SEND_MAIL_FORGOT_PASSWORD;

        authService.handleForgotPassword(email);

        return ResponseEntity.ok()
                .body(ApiResponse.builderResponse(successCode, null));
    }

    @GetMapping("/forgot-password/confirmation")
    public Object verifyForgotPasswordToken(@RequestParam String token, @RequestParam String id) {


        if (token == null || id == null) {
            return new RedirectView("http://localhost:5173/confirm-failure");
        }

        User user = userService.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getForgotPasswordToken().equals(token)) {

            log.info("successfully verified in forgot password token");


            return new RedirectView("http://localhost:5173/reset?email=" + user.getEmail() + "&token=" + token);

        } else {

            log.info("failed verified in forgot password token");

            return new RedirectView("http://localhost:5173/confirm-failure");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> handleResetPassword(@RequestParam String token, @RequestBody ChangePasswordRequest request) {

        SuccessCode successCode = SuccessCode.CHANGE_PASSWORD;

        authService.handleResetPassword(request, token);
        return ResponseEntity.ok(
                ApiResponse.builderResponse(successCode, null)
        );
    }

    @GetMapping("/account")
    public ResponseEntity<ApiResponse<User>> getAccount() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponse.builderResponse(SuccessCode.FETCHED, user)
        );
    }

    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Object>> sendMailVerifyAccount(@RequestParam String email) {

        this.authService.verifyAccount(email);
        SuccessCode successCode = SuccessCode.SEND_MAIL_VERIFY_ACCOUNT;

        return ResponseEntity.ok(
                ApiResponse.builderResponse(successCode, null)
        );

    }

    @GetMapping("/verify-account/confirmation")
    public Object verifyAccount(@RequestParam String token, @RequestParam String id) {

        if (token == null || id == null) {
            return new RedirectView("http://localhost:5173/confirm-failure");
        }

        User user = userService.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getVerificationToken().equals(token)) {

            user.setVerificationToken(null);
            user.setHasVerified(true);

            userService.saveUser(user);
            log.info("successfully verified in confirm account token");

            return new RedirectView("http://localhost:5173/confirm-success");

        } else {

            log.info("failed verified in confirm account token");

            return new RedirectView("http://localhost:5173/confirm-failure");
        }
    }
}
