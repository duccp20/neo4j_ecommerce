package com.neo4j_ecom.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;

import java.time.Instant;
import java.util.Optional;


@Configuration
@EnableNeo4jAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class Neo4jConfig {

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }
}
