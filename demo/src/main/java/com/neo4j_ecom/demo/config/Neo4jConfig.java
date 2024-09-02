package com.neo4j_ecom.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.data.neo4j.config.EnableReactiveNeo4jAuditing;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;


@Configuration
@EnableNeo4jAuditing
public class Neo4jConfig {

}
