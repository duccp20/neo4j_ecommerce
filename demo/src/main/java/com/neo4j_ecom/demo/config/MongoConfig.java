package com.neo4j_ecom.demo.config;

import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.repository.AuthRepository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfig implements AuditorAware<String> {

    @Autowired
     AccountRepository accountRepository;




    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountRepository.findByEmail(email).get();

            if(account != null) {
                return Optional.of(account.getEmail());
            }

            return Optional.empty();
//            return Optional.of(account.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
