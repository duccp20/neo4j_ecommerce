package com.neo4j_ecom.demo.config;

import com.mongodb.client.MongoClients;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfig implements AuditorAware<String> {

    private final UserRepository userRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            return Optional.of(user.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}