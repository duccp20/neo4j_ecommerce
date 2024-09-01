package com.neo4j_ecom.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {

        String firebaseConfigBase64 = System.getenv("FIREBASE_CONFIG");
        if (firebaseConfigBase64 == null || firebaseConfigBase64.isEmpty()) {
            throw new IllegalStateException("FIREBASE_CONFIG environment variable is not set");
        }

        byte[] decodedConfig = Base64.getDecoder().decode(firebaseConfigBase64);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decodedConfig));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket("ecom-accessed.appspot.com")
                .build();
        return FirebaseApp.initializeApp(options);
    }

}