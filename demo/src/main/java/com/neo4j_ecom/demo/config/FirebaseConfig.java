package com.neo4j_ecom.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
@Slf4j
public class FirebaseConfig {


    @Value("${firebase.secret}")
    private String secretBase64;
    @Bean
    public FirebaseApp initializeFirebase() throws IOException {


        byte[] decodedConfig = Base64.getDecoder().decode(secretBase64);

        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decodedConfig));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket("ecom-accessed.appspot.com")
                .build();
        return FirebaseApp.initializeApp(options);
    }

}