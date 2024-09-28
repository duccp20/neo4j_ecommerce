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

    @Override
    public Void handleForgotPassword(String email) {


        User user = userService.findByEmail(email);

        String token = UUID.randomUUID().toString();
        userService.updateForgotPasswordToken(token, user.getId());


        String domain = "http://localhost:8080";
        String link = domain + "/api/v1/auth/forgot-password/confirmation?token=" + token + "&id=" + user.getId();

        emailService.sendMailWithLink(email, "Forgot Password", "forgotPassword", user.getFirstName() + " " + user.getLastName(), link);

        return null;
    }

    @Override
    public Object verifyForgotPasswordToken(String token, String id) {
        User user = userService.findById(id);

        if (user.getForgotPasswordToken().equals(token)) {

            user.setForgotPasswordToken(null);

            log.info("successfully verified in forgot password token");

            return new RedirectView("https://www.youtube.com/watch?v=Llw9Q6akRo4&list=RDMMy576-ONm5II&index=25");

        } else {
            user.setForgotPasswordToken(null);

            log.info("failed verified in forgot password token");

            return new RedirectView("https://www.youtube.com/watch?v=-vtBgNxMyZI&list=RDMMy576-ONm5II&index=32");

        }
    }

    @Override
    public void verifyAccount(String email) {

        User user = userService.findByEmail(email);

        String token = UUID.randomUUID().toString();
        userService.updateVerificationToken(token, user.getId());


        String domain = "http://localhost:8080";
        String link = domain + "/api/v1/auth/verify-account/confirmation?token=" + token + "&id=" + user.getId();

        emailService.sendMailWithLink(email, "Confirm Registration", "confirmRegistration", user.getFirstName() + " " + user.getLastName(), link);

    }

    @Override
    public void handleResetPassword(ChangePasswordRequest request) {

        User user = userService.findByEmail(request.getEmail());

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setForgotPasswordToken(null);
        userRepository.save(user);

    }

}
