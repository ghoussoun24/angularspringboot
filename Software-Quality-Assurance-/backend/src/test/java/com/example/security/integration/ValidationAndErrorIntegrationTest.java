package com.example.security.integration;

import com.example.security.Controller.PasswordResetController;
import com.example.security.services.EmailService;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import com.example.security.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(com.example.security.config.TestSecurityConfig.class)
@DisplayName("Tests d'Intégration - Validation et Gestion d'Erreurs")
class ValidationAndErrorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        // Mock EmailService pour ne rien envoyer
        doNothing().when(emailService).sendResetCode(anyString(), anyString());

        // Nettoyage et création d’un utilisateur test
        userRepository.deleteAll();
        User user = User.builder()
                .firstname("Test")
                .lastname("User")
                .email("test@example.com")
                .password(passwordEncoder.encode("OldPassword123!"))
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("POST /request - Email invalide retourne 400")
    void shouldReturnBadRequestForInvalidEmail() throws Exception {
        PasswordResetController.EmailRequest invalidEmail = new PasswordResetController.EmailRequest();
        invalidEmail.setEmail("invalid-email");

        mockMvc.perform(post("/api/v1/password/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /request - Email valide retourne 200")
    void shouldReturnOkForValidEmail() throws Exception {
        PasswordResetController.EmailRequest validEmail = new PasswordResetController.EmailRequest();
        validEmail.setEmail("test@example.com");

        mockMvc.perform(post("/api/v1/password/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEmail)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /reset - Mot de passe trop court retourne 400")
    void shouldReturnBadRequestForShortPassword() throws Exception {
        PasswordResetController.ResetRequest shortPassword = new PasswordResetController.ResetRequest();
        shortPassword.setEmail("test@example.com");
        shortPassword.setNewPassword("123");

        mockMvc.perform(post("/api/v1/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /reset - Mot de passe valide retourne 200")
    void shouldReturnOkForValidPassword() throws Exception {
        PasswordResetController.ResetRequest validPassword = new PasswordResetController.ResetRequest();
        validPassword.setEmail("test@example.com");
        validPassword.setNewPassword("ValidPass123!");

        mockMvc.perform(post("/api/v1/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPassword)))
                .andExpect(status().isOk());
    }
}
