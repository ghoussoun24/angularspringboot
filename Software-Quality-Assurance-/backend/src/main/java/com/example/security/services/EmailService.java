package com.example.security.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.email.enabled", havingValue = "true", matchIfMissing = true)

public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sghoussoun@gmail.com"); // OBLIGATOIRE ElasticEmail
            message.setTo(toEmail);
            message.setSubject("Code de réinitialisation du mot de passe");
            message.setText(
                    "Bonjour,\n\n" +
                            "Votre code de réinitialisation est : " + code + "\n\n" +
                            "Ce code expire dans 15 minutes.\n\n" +
                            "Si vous n'avez rien demandé, ignorez cet email.\n\n" +
                            "Support Technique."
            );

            mailSender.send(message);
            log.info("Email envoyé à {}", toEmail);
        } catch (Exception e) {
            log.error("Erreur envoi mail : {}", e.getMessage());
            throw new RuntimeException("Impossible d'envoyer l'email de réinitialisation.");
        }
    }
}
