package com.domuswap.controller;

import com.domuswap.model.User;
import com.domuswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String password = payload.get("password");
            String firstName = payload.get("firstName");
            String lastName = payload.get("lastName");
            String phoneNumber = payload.get("phoneNumber");

            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(password); // Note: In production, this should be hashed
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setRole("USER");
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            logger.error("Error registering user", e);
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload, HttpSession session) {
        try {
            String email = payload.get("email");
            String password = payload.get("password");

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid email or password");
            }

            User user = userOpt.get();
            if (!user.getPassword().equals(password)) { // Note: In production, use proper password comparison
                return ResponseEntity.badRequest().body("Invalid email or password");
            }

            session.setAttribute("loggedInUser", user);
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "role", user.getRole()
                )
            ));
        } catch (Exception e) {
            logger.error("Error logging in", e);
            return ResponseEntity.badRequest().body("Error logging in: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<?> checkAuth(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "user", Map.of(
                "id", loggedInUser.getId(),
                "email", loggedInUser.getEmail(),
                "firstName", loggedInUser.getFirstName(),
                "lastName", loggedInUser.getLastName(),
                "role", loggedInUser.getRole()
            )
        ));
    }

    @PostMapping("/update-profile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> payload, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to update profile");
        }

        try {
            String firstName = payload.get("firstName");
            String lastName = payload.get("lastName");
            String phoneNumber = payload.get("phoneNumber");

            loggedInUser.setFirstName(firstName);
            loggedInUser.setLastName(lastName);
            loggedInUser.setPhoneNumber(phoneNumber);

            userRepository.save(loggedInUser);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }
} 