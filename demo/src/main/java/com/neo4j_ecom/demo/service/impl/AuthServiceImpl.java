package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.UserRepository;
import com.neo4j_ecom.demo.service.AuthService;
import com.neo4j_ecom.demo.service.EmailService;
import com.neo4j_ecom.demo.service.UserService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final UserService userService;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Value("${domain}")
    private String domain;

    @Override
    public Void handleForgotPassword(String email) {


        User user = userService.findByEmail(email);

        String token = UUID.randomUUID().toString();
        userService.updateForgotPasswordToken(token, user.getId());

        String link = domain + "/auth/forgot-password/confirmation?token=" + token + "&id=" + user.getId();

        emailService.sendMailWithLink(email, "Forgot Password", "forgotPassword", user.getFirstName() + " " + user.getLastName(), link);

        return null;
    }

    @Override
    public void verifyAccount(String email) {

        User user = userService.findByEmail(email);

        String token = UUID.randomUUID().toString();
        userService.updateVerificationToken(token, user.getId());

        String link = domain + "/auth/verify-account/confirmation?token=" + token + "&id=" + user.getId();

        log.info("link: {}", link);

        emailService.sendMailWithLink(email, "Confirm Registration", "confirmRegistration", user.getFirstName() + " " + user.getLastName(), link);

    }

    @Override
    public void handleResetPassword(ChangePasswordRequest request, String token) {

        User user = userService.findByEmail(request.getEmail());

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
