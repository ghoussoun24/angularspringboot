package com.example.security.config;

import com.example.security.services.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mockito.Mockito;
@Configuration
public class TestEmailConfig {

    @Bean
    public EmailService mailService() {
        EmailService mock = Mockito.mock(EmailService.class);
        // Mock pour que sendResetCode ne fasse rien
        Mockito.doNothing().when(mock).sendResetCode(Mockito.anyString(), Mockito.anyString());
        return mock;
    }
}
