package com.neo4j_ecom.demo.config;

import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.repository.AuthRepository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfig implements AuditorAware<String> {

    private final AccountRepository accountRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountRepository.findByEmail(email).orElseThrow();
            return Optional.of(account.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}