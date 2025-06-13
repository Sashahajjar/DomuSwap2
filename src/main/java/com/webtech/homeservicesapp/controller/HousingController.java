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
        this.uploadDir = Paths.get(System.getProperty("user.home"), "uploads");
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

    @PostMapping("/addWithImage")
    public ResponseEntity<?> addHousingWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("constraint_text") String constraintText,
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("services") String services,
            @RequestParam("amenities") String amenities,
            @RequestParam("maxGuests") Integer maxGuests,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            System.out.println("Received file upload request");
            System.out.println("Original filename: " + imageFile.getOriginalFilename());
            System.out.println("Content type: " + imageFile.getContentType());
            System.out.println("File size: " + imageFile.getSize());

            if (imageFile.isEmpty()) {
                System.out.println("Error: Empty file received");
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            // Generate a unique filename
            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String fileName = UUID.randomUUID().toString() + extension;
            
            // Save file to uploads directory
            Path filePath = uploadDir.resolve(fileName);
            System.out.println("Attempting to save file to: " + filePath.toAbsolutePath());
            
            // Create parent directories if they don't exist
            Files.createDirectories(filePath.getParent());
            
            // Save the file
            try (var inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            System.out.println("File saved successfully at: " + filePath.toAbsolutePath());
            
            // Verify file exists and is readable
            if (Files.exists(filePath)) {
                System.out.println("File exists after save. Size: " + Files.size(filePath) + " bytes");
                System.out.println("File is readable: " + Files.isReadable(filePath));
                System.out.println("File permissions: " + Files.getPosixFilePermissions(filePath));
            } else {
                System.out.println("WARNING: File does not exist after save!");
                return ResponseEntity.internalServerError().body("Failed to save file");
            }

            // Build DTO with the upload path in photo_1
            HousingWithServicesDTO dto = new HousingWithServicesDTO();
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setLocation(location);
            dto.setConstraint_text(constraintText);
            dto.setOwnerId(ownerId);
            String photoPath = "/uploads/" + fileName;
            System.out.println("Setting photo path to: " + photoPath);
            dto.setPhoto_1(photoPath);
            dto.setServices(Arrays.stream(services.split(","))
                                  .map(String::trim)
                                  .collect(Collectors.toList()));
            dto.setAmenities(amenities);
            dto.setMaxGuests(maxGuests);

            // Delegate to service
            return housingService.addHousingWithServices(dto);

        } catch (IOException e) {
            System.err.println("Error during file upload: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                                 .body("Image upload failed: " + e.getMessage());
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
            
            // Handle image update if a new image is provided
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                String imagePath = saveImage(dto.getImageFile());
                housing.setPhoto_1(imagePath);
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
