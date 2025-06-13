package com.webtech.homeservicesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.model.PasswordResetToken;
import com.webtech.homeservicesapp.repository.UserRepository;
import com.webtech.homeservicesapp.repository.PasswordResetTokenRepository;

@Service
public class AuthService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void createPasswordResetToken(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                String token = UUID.randomUUID().toString();
                LocalDateTime expiry = LocalDateTime.now().plusHours(1);

                PasswordResetToken resetToken = new PasswordResetToken();
                resetToken.setToken(token);
                resetToken.setEmail(email);
                resetToken.setExpiryDate(expiry);
                tokenRepository.save(resetToken);

                // Send real email
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("DomuSwap Password Reset");
                message.setText("You requested a password reset. Click the link below to reset your password:\n" +
                    "http://localhost:8080/reset_password.html?token=" + token +
                    "\n\nIf you did not request this, please ignore this email.");
                mailSender.send(message);
            } else {
                System.out.println("User not found for email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Debugging forgot-password failure", e);
        }
    }

    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        System.out.println("Attempting to reset password with token: " + token);
        
        // Validate token
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (!tokenOpt.isPresent()) {
            System.out.println("Token not found in database: " + token);
            throw new RuntimeException("Invalid or expired token.");
        }

        // Validate password
        if (newPassword == null || newPassword.length() < 8) {
            System.out.println("Invalid password length: " + (newPassword == null ? "null" : newPassword.length()));
            throw new RuntimeException("Password must be at least 8 characters long.");
        }

        // Validate password confirmation
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match");
            throw new RuntimeException("Passwords do not match.");
        }

        PasswordResetToken resetToken = tokenOpt.get();
        System.out.println("Found token for email: " + resetToken.getEmail() + ", expires: " + resetToken.getExpiryDate());
        
        // Check token expiration
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            System.out.println("Token expired at: " + resetToken.getExpiryDate());
            tokenRepository.deleteByToken(token);
            throw new RuntimeException("Token has expired.");
        }

        // Find and update user
        Optional<User> userOpt = userRepository.findByEmail(resetToken.getEmail());
        if (!userOpt.isPresent()) {
            System.out.println("User not found for email: " + resetToken.getEmail());
            throw new RuntimeException("User not found.");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        System.out.println("Password reset successful for user: " + user.getEmail());
        
        // Delete used token
        tokenRepository.deleteByToken(token);
    }
} 