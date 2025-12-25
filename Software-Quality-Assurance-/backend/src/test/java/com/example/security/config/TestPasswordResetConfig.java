package com.example.security.config;

import com.example.security.services.EmailService;
import com.example.security.services.PasswordResetService;
import com.example.security.token.PasswordResetTokenRepository;
import com.example.security.user.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestPasswordResetConfig {
    @Bean
    public PasswordResetService passwordResetService(UserRepository userRepository,
                                                     PasswordResetTokenRepository tokenRepository,
                                                     EmailService emailService,
                                                     PasswordEncoder passwordEncoder) {
        return new PasswordResetService(userRepository, tokenRepository, emailService, passwordEncoder);
    }
}