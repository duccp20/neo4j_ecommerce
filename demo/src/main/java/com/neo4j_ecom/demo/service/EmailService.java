package com.neo4j_ecom.demo.service;

public interface EmailService {
    void sendMailWithLink(String to, String subject, String templateName, String userName, Object value);
}
