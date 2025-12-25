package com.example.security.api;

import com.example.security.services.EmailService;
import com.example.security.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({com.example.security.config.TestSecurityConfig.class,
        com.example.security.config.TestJwtConfig.class,
        com.example.security.config.TestEmailConfig.class,
        com.example.security.config.TestPasswordResetConfig.class})
public class PasswordResetApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Mock EmailService pour ne rien envoyer
        doNothing().when(emailService).sendResetCode(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void testRequestPasswordReset_ShouldReturnSuccess() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"test@example.com\"}")
                .when()
                .post("/api/v1/password/request")
                .then()
                .statusCode(200)
                .body("message", equalTo("Code envoyé avec succès."));
    }

    @Test
    void testRequestPasswordReset_WithInvalidEmail_ShouldReturnBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"invalid-email\"}")
                .when()
                .post("/api/v1/password/request")
                .then()
                .statusCode(400);
    }

    @Test
    void testRequestPasswordReset_WithEmptyEmail_ShouldReturnBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"\"}")
                .when()
                .post("/api/v1/password/request")
                .then()
                .statusCode(400);
    }
}
