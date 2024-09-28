package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.LoginRequest;
import com.neo4j_ecom.demo.model.dto.request.RegisterRequest;
import com.neo4j_ecom.demo.model.dto.request.TokenRefreshRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.AuthResponse;
import com.neo4j_ecom.demo.model.dto.response.TokenRefreshResponse;
import com.neo4j_ecom.demo.model.dto.response.UserResponse;
import com.neo4j_ecom.demo.model.entity.RefreshToken;
import com.neo4j_ecom.demo.model.entity.Role;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.security.JwtUtil;
import com.neo4j_ecom.demo.service.RefreshTokenService;
import com.neo4j_ecom.demo.service.UserService;
import com.neo4j_ecom.demo.service.impl.RefreshTokenServiceImpl;
import com.neo4j_ecom.demo.service.impl.UserDetailsImpl;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import com.neo4j_ecom.demo.utils.enums.TokenRefreshException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        SuccessCode successCode = SuccessCode.REGISTER;
        userService.registerUser(registerRequest);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = userService.findByEmail(email);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            String token = jwtUtil.generateTokenFromUsername(user.getEmail());
            String refreshTokenJwt = refreshToken.getRefreshToken();

            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                    .build();

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshTokenJwt);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshTokenCookie);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setUser(userResponse);
            authResponse.setToken(token);

            SuccessCode successCode = SuccessCode.LOGIN;

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .statusCode(successCode.getCode())
                            .message(successCode.getMessage())
                            .data(authResponse)
                            .build()
            );
        } catch (BadCredentialsException ex) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse
            response)
    {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; ++i) {
            if (cookies[i].getName().equals("refresh_token")) {
                cookies[i].setValue(null);
                cookies[i].setMaxAge(0);
                cookies[i].setSecure(true);
                cookies[i].setHttpOnly(true);
                cookies[i].setPath("/");

                response.addCookie(cookies[i]);
                break;
            }
        }

        SuccessCode successCode = SuccessCode.LOGOUT;

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .build()
        );
    }


    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refresh_token") String requestRefreshToken) {

        log.info("requestRefreshToken: {}", requestRefreshToken);
        RefreshToken oldRefreshToken = refreshTokenService.findByToken(requestRefreshToken);

        refreshTokenService.verifyExpiration(oldRefreshToken);

        this.jwtUtil.validateJwtToken(oldRefreshToken.getRefreshToken());

        String accessToken = this.jwtUtil.generateTokenFromUsername(oldRefreshToken.getUser().getEmail());

        String refreshToken = this.jwtUtil.generateRefreshToken(oldRefreshToken.getUser().getEmail());

        oldRefreshToken.setRefreshToken(refreshToken);
        refreshTokenService.saveRefreshToken(oldRefreshToken);

        Cookie refreshTokenCookie = new Cookie("refresh_token", oldRefreshToken.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        SuccessCode successCode = SuccessCode.TOKEN_REFRESH;

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.builder()
                        .statusCode(successCode.getCode())
                        .data(new TokenRefreshResponse(accessToken))
                        .message(successCode.getMessage())
                        .build()
                );
    }


}
