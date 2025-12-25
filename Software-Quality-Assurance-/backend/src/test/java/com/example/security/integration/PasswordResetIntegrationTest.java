package com.example.security.integration;

import com.example.security.user.Role;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
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
@DisplayName("Intégration - Réinitialisation mot de passe")
class PasswordResetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private com.example.security.services.EmailService emailService;

    @BeforeEach
    void setUp() {
        doNothing().when(emailService).sendResetCode(anyString(), anyString());

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
    @DisplayName("POST /request - succès")
    void shouldAcceptPasswordResetRequest() throws Exception {
        String request = "{\"email\":\"test@example.com\"}";

        mockMvc.perform(post("/api/v1/password/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }
}
