package com.domuswap.controller;

import com.domuswap.model.HousingDTO;
import com.domuswap.model.HousingWithServicesDTO;
import com.domuswap.model.Housing;
import com.domuswap.model.RequestedService;
import com.domuswap.service.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.io.InputStream;

@RestController
@RequestMapping("/api/housing")
@CrossOrigin(origins = "*")
public class HousingController {
    @Autowired
    private HousingService housingService;
    private final Path uploadDir;

    public HousingController() {
        this.uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        System.out.println("Upload directory path: " + uploadDir.toAbsolutePath());
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                System.out.println("Created upload directory at: " + uploadDir.toAbsolutePath());
            } else {
                System.out.println("Upload directory already exists at: " + uploadDir.toAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addHousingWithServices(@RequestBody HousingWithServicesDTO dto) {
        return housingService.addHousingWithServices(dto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchHousing(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String checkIn,
            @RequestParam(required = false) String checkOut,
            @RequestParam(required = false) Integer person) {
        return ResponseEntity.ok(housingService.searchByFilters(location, checkIn, checkOut, person));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getHousingByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(housingService.getByOwnerId(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHousingById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(housingService.getHousingWithServices(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting housing: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHousing(@PathVariable Long id, @RequestBody HousingWithServicesDTO dto) {
        return housingService.updateHousing(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHousing(@PathVariable Long id) {
        return housingService.deleteHousing(id);
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new IOException("Could not store file " + fileName, e);
        }
    }
} 