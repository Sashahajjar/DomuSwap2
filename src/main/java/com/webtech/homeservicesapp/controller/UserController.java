package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.service.UserService;

import com.webtech.homeservicesapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    private final Path profilePicStorage = Paths.get("uploads/profile_pics");

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

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (!Files.exists(profilePicStorage)) {
                Files.createDirectories(profilePicStorage);
            }
            String filename = "user_" + id + "_" + System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = profilePicStorage.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Update user profilePicture field
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setProfilePicture("/uploads/profile_pics/" + filename);
                userService.save(user);
            }
            return ResponseEntity.ok(Map.of("url", "/api/users/" + id + "/profile-picture?ts=" + System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }

    @GetMapping("/{id}/profile-picture")
    public ResponseEntity<?> getProfilePicture(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent() && userOpt.get().getProfilePicture() != null) {
                String picPath = userOpt.get().getProfilePicture();
                Path filePath = Paths.get(".").resolve(picPath.replaceFirst("/", ""));
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists()) {
                    return ResponseEntity.ok()
                        .header("Content-Type", Files.probeContentType(filePath))
                        .body(resource);
                }
            }
            // Return default avatar if not found
            Path defaultPic = Paths.get("src/main/resources/static/images/default_avatar.png");
            Resource resource = new UrlResource(defaultPic.toUri());
            return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile picture not found");
        }
    }
}
