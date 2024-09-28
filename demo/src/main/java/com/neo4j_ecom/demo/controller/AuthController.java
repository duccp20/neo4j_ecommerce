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

            //  Set refresh token to cookie
            ResponseCookie responseCookie = ResponseCookie
                    .from("refresh_token", refreshTokenJwt)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 30) // 30 days)
                    .build();

            SuccessCode successCode = SuccessCode.LOGIN;

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(
                            ApiResponse.builder()
                                    .statusCode(successCode.getCode())
                                    .message(successCode.getMessage())
                                    .data(userResponse)
                                    .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            Cookie cookie = new Cookie("refresh_token", null);
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
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
    public ResponseEntity<?> refreshToken(@CookieValue("refresh_token") String requestRefreshToken, HttpServletResponse response) {

        log.info("requestRefreshToken: {}", requestRefreshToken);

        if (requestRefreshToken == null) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
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
        response.addCookie(refreshTokenCookie);

        SuccessCode successCode = SuccessCode.TOKEN_REFRESH;

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .statusCode(successCode.getCode())
                        .data(new TokenRefreshResponse(accessToken))
                        .message(successCode.getMessage())
                        .build()
                );
    }


}
