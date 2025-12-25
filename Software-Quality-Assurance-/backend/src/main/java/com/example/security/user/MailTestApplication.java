package com.example.security.user;

import com.example.security.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MailTestApplication implements CommandLineRunner {

    private final EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(MailTestApplication.class, args);
    }

    @Override
    public void run(String... args) {
        emailService.sendResetCode("sghoussoun@gmail.com", "123456");
    }
}
