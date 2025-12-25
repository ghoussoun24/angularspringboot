package com.example.security.services;


import com.example.security.token.PasswordResetToken;
import com.example.security.token.PasswordResetTokenRepository;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ---- 1) DEMANDER RESET (EMAIL → CODE) ----
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Supprimer anciens codes
        tokenRepository.deleteByUser(user);

        // Générer un code à 6 chiffres
        String code = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken token = PasswordResetToken.builder()
                .user(user)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .attempts(0)
                .build();

        tokenRepository.save(token);

        // envoyer email
        emailService.sendResetCode(user.getEmail(), code);
    }

    // ---- 2) VALIDATION DU CODE ----
    public boolean validateCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        PasswordResetToken token = tokenRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Code invalide"));

        if (!token.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Code ne correspond pas à cet utilisateur.");

        if (token.isExpired())
            throw new RuntimeException("Code expiré !");

        if (token.isUsed())
            throw new RuntimeException("Code déjà utilisé !");

        token.setUsed(true);
        tokenRepository.save(token);

        return true;
    }

    // ---- 3) CHANGER LE MOT DE PASSE ----
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
