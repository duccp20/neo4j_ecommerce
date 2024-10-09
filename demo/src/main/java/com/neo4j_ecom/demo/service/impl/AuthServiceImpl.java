package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.config.JwtConfig;
import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.dto.request.RegisterRequest;
import com.neo4j_ecom.demo.model.entity.Role;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.RoleRepository;
import com.neo4j_ecom.demo.repository.UserRepository;
import com.neo4j_ecom.demo.service.AuthService;
import com.neo4j_ecom.demo.service.EmailService;
import com.neo4j_ecom.demo.service.UserService;
import com.neo4j_ecom.demo.utils.enums.ERole;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${domain}")
    private String domain;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtConfig jwtConfig;

    private final EmailService emailService;


    @Value("${jwt.refresh-expiration}")
    private long maxAge;


    @Override
    public User register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setHasVerified(false);

        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()) {
            registerRequest.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                roles.add(role);
            });
        } else {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_USER.name())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(defaultRole);
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> login(Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {
            throw new AppException(ErrorCode.MISSING_REQUIRE_PARAM);
        }

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = this.userService.getUserByEmail(emailUser).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (!user.isHasVerified()) {
            throw new AppException(ErrorCode.USER_NOT_VERIFIED);
        }

        if (!passwordEncoder.matches(request.get("password"), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtConfig.generateToken(user, "access");

        Map<String, Object> response = new HashMap<>();
        response.put("token", accessToken);
        response.put("user", user);

        return response;



    }

    @Override
    public Void handleForgotPassword(String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String token = UUID.randomUUID().toString();
        userService.updateForgotPasswordToken(token, user.getId());

        String link = domain + "/auth/forgot-password/confirmation?token=" + token + "&id=" + user.getId();

        emailService.sendMailWithLink(email, "Forgot Password", "forgotPassword", user.getFirstName() + " " + user.getLastName(), link);

        return null;
    }

    @Override
    public void verifyAccount(String email) {

        User user = userService.getUserByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        String token = UUID.randomUUID().toString();
        userService.updateVerificationToken(token, user.getId());

        String link = domain + "/auth/verify-account/confirmation?token=" + token + "&id=" + user.getId();

        log.info("link: {}", link);

        emailService.sendMailWithLink(email, "Confirm Registration", "confirmRegistration", user.getFirstName() + " " + user.getLastName(), link);

    }

    @Override
    public void handleResetPassword(ChangePasswordRequest request, String token) {
        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getForgotPasswordToken() == null || !user.getForgotPasswordToken().equals(token)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setForgotPasswordToken(null);
        userRepository.save(user);
    }

}
