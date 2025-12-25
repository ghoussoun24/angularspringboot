package com.example.security.token;

import com.example.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByCodeAndUser(String code, User user);
    Optional<PasswordResetToken> findByCode(String code);
    void deleteByUser(User user);
    void deleteByExpiryDateBefore(LocalDateTime date);
}
