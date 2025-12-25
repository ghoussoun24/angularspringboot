package com.example.security.Controller;

import com.example.security.services.PasswordResetService;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    public PasswordResetController(PasswordResetService passwordResetService, UserRepository userRepository) {
        this.passwordResetService = passwordResetService;
        this.userRepository = userRepository;
    }


    // ===================== 1) REQUEST RESET =====================
    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody EmailRequest request) {
        if (request.getEmail() == null || !request.getEmail().matches(".+@.+\\..+")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email invalide."));
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            // Retourner succès même si l'utilisateur n'existe pas pour éviter la fuite d'info
            return ResponseEntity.ok(Map.of("message", "Code envoyé avec succès."));
        }

        // Appeler le service pour envoyer le code
        passwordResetService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Code envoyé avec succès."));
    }


    // ===================== 2) VERIFY CODE =====================
    @PostMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyCode(
            @Valid @RequestBody VerifyCodeRequest request) {

        boolean valid = passwordResetService.validateCode(
                request.getEmail(),
                request.getCode()
        );

        return ResponseEntity.ok(
                Map.of(
                        "valid", valid,
                        "message", "Code valide"
                )
        );
    }

    // ===================== 3) RESET PASSWORD =====================
    @PostMapping(value = "/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetRequest request) {

        passwordResetService.updatePassword(
                request.getEmail(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                Map.of("message", "Mot de passe modifié avec succès.")
        );
    }

    // ===================== DTOs =====================
    @Data
    public static class EmailRequest {

        @NotBlank(message = "Email obligatoire")
        @Email(message = "Format email invalide")
        private String email;
    }

    @Data
    public static class VerifyCodeRequest {

        @NotNull(message = "Email obligatoire")
        @NotBlank(message = "Email obligatoire")
        private String email;

        @NotBlank(message = "Code obligatoire")
        private String code;
    }

    @Data
    public static class ResetRequest {

        @NotBlank(message = "Email obligatoire")
        @Email(message = "Format email invalide")
        private String email;

        @NotBlank(message = "Mot de passe obligatoire")
        @Size(min = 8, max = 100, message = "Mot de passe invalide")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
                message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial"
        )
        private String newPassword;
    }
}
