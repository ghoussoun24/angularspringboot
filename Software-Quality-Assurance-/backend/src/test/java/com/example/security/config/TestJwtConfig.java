package com.example.security.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestJwtConfig {

    @Bean
    @Primary
    public String testJwtSecretKey() {
        return "test-secret-key-for-testing-purposes-only-that-must-be-at-least-256-bits-long-for-security";
    }

    @Bean
    @Primary
    public Long testJwtExpiration() {
        return 3600000L; // 1 hour
    }
}