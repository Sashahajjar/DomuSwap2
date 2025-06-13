package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.service.UserService;

import com.webtech.homeservicesapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
    	String name = payload.get("name");
    	String email = payload.get("email");
    	String password = payload.get("password");
    	String phone = payload.get("phone");
    	String address = payload.get("address");
    	String role = payload.get("role"); // ✅ Accept role


        Optional<User> existing = userService.login(email, password); // Or a proper exists check
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        User created = userService.register(name, email, password, phone, address, role);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> user = userService.login(email, password);
        if (user.isPresent()) {
            User loggedInUser = user.get();
            session.setAttribute("loggedInUser", loggedInUser);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            return ResponseEntity.ok()
                .header("Set-Cookie", "JSESSIONID=" + session.getId() + "; Path=/; HttpOnly")
                .body(loggedInUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}
