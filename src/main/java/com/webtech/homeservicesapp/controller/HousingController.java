package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.HousingDTO;
import com.webtech.homeservicesapp.model.HousingWithServicesDTO;
import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.RequestedService;
import com.webtech.homeservicesapp.service.HousingService;
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
@CrossOrigin(origins = "*")   // allow calls from your front-end
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

    @PostMapping("/addWithImages")
    public ResponseEntity<?> addHousingWithImages(
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("location") String location,
        @RequestParam("constraint_text") String constraintText,
        @RequestParam("ownerId") Long ownerId,
        @RequestParam("services") String services,
        @RequestParam("amenities") String amenities,
        @RequestParam("maxGuests") Integer maxGuests,
        @RequestParam(value = "image1", required = false) MultipartFile image1,
        @RequestParam(value = "image2", required = false) MultipartFile image2,
        @RequestParam(value = "image3", required = false) MultipartFile image3
    ) {
        try {
            HousingWithServicesDTO dto = new HousingWithServicesDTO();
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setLocation(location);
            dto.setConstraint_text(constraintText);
            dto.setOwnerId(ownerId);
            dto.setServices(Arrays.stream(services.split(",")).map(String::trim).collect(Collectors.toList()));
            dto.setAmenities(amenities);
            dto.setMaxGuests(maxGuests);

            if (image1 != null && !image1.isEmpty()) dto.setPhoto_1(saveImage(image1));
            if (image2 != null && !image2.isEmpty()) dto.setPhoto_2(saveImage(image2));
            if (image3 != null && !image3.isEmpty()) dto.setPhoto_3(saveImage(image3));

            return housingService.addHousingWithServices(dto);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Image upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<HousingDTO>> search(@RequestParam String location) {
        return ResponseEntity.ok(housingService.searchByLocation(location));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<HousingDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(housingService.getByOwnerId(ownerId));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getHousingDetails(@PathVariable Long id) {
        try {
            Housing housing = housingService.getHousingById(id);
            // Force initialization of services
            Hibernate.initialize(housing.getServices());
            
            // Convert to DTO
            HousingWithServicesDTO dto = new HousingWithServicesDTO();
            dto.setTitle(housing.getTitle());
            dto.setDescription(housing.getDescription());
            dto.setLocation(housing.getLocation());
            dto.setPhoto_1(housing.getPhoto_1());
            dto.setPhoto_2(housing.getPhoto_2());
            dto.setPhoto_3(housing.getPhoto_3());
            dto.setConstraint_text(housing.getConstraint_text());
            dto.setOwnerId(housing.getOwner().getId());
            dto.setAmenities(housing.getAmenities());
            dto.setMaxGuests(housing.getMaxGuests());
            dto.setServices(
                housing.getServices()
                    .stream()
                    .map(RequestedService::getTitle)
                    .collect(Collectors.toList())
            );
            
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateHousing(@PathVariable Long id, @ModelAttribute HousingWithServicesDTO dto) {
        try {
            // Get the existing housing
            Housing housing = housingService.getHousingById(id);
            
            // Update basic fields
            housing.setTitle(dto.getTitle());
            housing.setDescription(dto.getDescription());
            housing.setLocation(dto.getLocation());
            housing.setMaxGuests(dto.getMaxGuests());
            housing.setAmenities(dto.getAmenities());
            
            // Handle image updates if new images are provided
            if (dto.getImage1() != null && !dto.getImage1().isEmpty()) {
                String imagePath = saveImage(dto.getImage1());
                housing.setPhoto_1(imagePath);
            }
            if (dto.getImage2() != null && !dto.getImage2().isEmpty()) {
                String imagePath = saveImage(dto.getImage2());
                housing.setPhoto_2(imagePath);
            }
            if (dto.getImage3() != null && !dto.getImage3().isEmpty()) {
                String imagePath = saveImage(dto.getImage3());
                housing.setPhoto_3(imagePath);
            }
            
            // Update services
            List<RequestedService> services = new ArrayList<>();
            if (dto.getServices() != null) {
                for (String serviceTitle : dto.getServices()) {
                    RequestedService service = new RequestedService();
                    service.setTitle(serviceTitle);
                    service.setHousing(housing);
                    services.add(service);
                }
            }
            housing.setServices(services);
            
            // Save the updated housing
            housingService.updateHousing(id, dto);
            
            return ResponseEntity.ok("Housing updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating housing: " + e.getMessage());
        }
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHousing(@PathVariable Long id) {
        return housingService.deleteHousing(id);
    }
}
