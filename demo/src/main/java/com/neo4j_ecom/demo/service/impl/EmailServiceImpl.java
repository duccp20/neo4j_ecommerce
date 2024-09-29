package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final org.springframework.mail.javamail.JavaMailSender javaMailSender;
    @Async
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
                              boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                    isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }

    @Override
    public void sendMailWithLink(String to, String subject, String templateName, String userName, Object value) {


        //get template and convert to string
        Context context = new Context();


        context.setVariable("link", value);
        String content = this.templateEngine.process(templateName, context);

        //send email
        this.sendEmailSync(to, subject, content, false, true);
    }
}
