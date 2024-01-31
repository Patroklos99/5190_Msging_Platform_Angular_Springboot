package com.inf5190.chat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Application spring boot.
 */
@SpringBootApplication
public class ChatApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatApplication.class);

    public static void main(String[] args) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("/home/kaped/IdeaProjects/5190_Messaging_Platform_1/backend/firebase-key.json");
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                LOGGER.info("Initializing Firebase application.");
                FirebaseApp.initializeApp(options);
            }
            LOGGER.info("Firebase application already initialized.");
            SpringApplication.run(ChatApplication.class, args);
        } catch (IOException e) {
            System.err.println("Could not initialise application. Please check your service account key path");
        }
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
