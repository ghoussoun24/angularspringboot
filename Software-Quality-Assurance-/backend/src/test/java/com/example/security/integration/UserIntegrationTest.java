package com.example.security.integration;

import com.example.security.config.TestEmailConfig;
import com.example.security.config.TestJwtConfig;
import com.example.security.config.TestSecurityConfig;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import com.example.security.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import({TestSecurityConfig.class, TestJwtConfig.class, TestEmailConfig.class})

@DisplayName("Tests d'Intégration - Utilisateurs")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .firstname("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("GET /users sans authentification")
    void getUsers_WithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/users/getUsers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /users avec rôle ADMIN")
    void getUsers_WithAdminRole() throws Exception {
        mockMvc.perform(get("/api/v1/users/getUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}