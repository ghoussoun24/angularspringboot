package com.example.security.unit;

import com.example.security.Controller.PasswordResetController;
import com.example.security.config.GlobalExceptionHandler;
import com.example.security.services.PasswordResetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires - PasswordResetController")
class PasswordResetControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private com.example.security.user.UserRepository userRepository;

    @Mock
    private com.example.security.user.User user;

    private PasswordResetController passwordResetController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        passwordResetController = new PasswordResetController(passwordResetService, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(passwordResetController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /request - succès email valide")
    void requestPasswordReset_ShouldReturnSuccess() throws Exception {
        doNothing().when(passwordResetService).requestPasswordReset(anyString());
        when(userRepository.findByEmail(eq("test@example.com"))).thenReturn(Optional.of(user));

        String request = "{\"email\":\"test@example.com\"}";

        mockMvc.perform(post("/api/v1/password/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Code envoyé avec succès."));

        verify(passwordResetService, times(1)).requestPasswordReset("test@example.com");
    }
}
