package com.example.security.unit;

import com.example.security.user.User;
import com.example.security.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserModelTest {

    @Test
    void testUserCreation() {
        // Arrange & Act
        User user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        // Assert
        assertEquals(1, user.getId());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("john@example.com", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testGetAuthorities() {
        // Arrange
        User user = User.builder()
                .role(Role.ADMIN)
                .build();

        // Act
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) user.getAuthorities();

        // Assert
        assertThat(authorities)
                .extracting("authority")
                .contains(
                        "ROLE_ADMIN",
                        "admin:read",
                        "admin:create",
                        "admin:update",
                        "admin:delete"
                );
    }

    @Test
    void testUserEquality() {
        // Arrange
        User user1 = User.builder()
                .id(1)
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .id(1)
                .email("test@example.com")
                .build();

        User user3 = User.builder()
                .id(2)
                .email("other@example.com")
                .build();

        // Assert
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testUserToString() {
        // Arrange
        User user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .build();

        // Act
        String toString = user.toString();

        // Assert
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john@example.com"));
    }
}