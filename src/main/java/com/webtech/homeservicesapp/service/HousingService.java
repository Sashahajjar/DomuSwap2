package com.webtech.homeservicesapp.service;

import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.HousingDTO;
import com.webtech.homeservicesapp.model.HousingWithServicesDTO;
import com.webtech.homeservicesapp.model.RequestedService;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.repository.HousingRepository;
import com.webtech.homeservicesapp.repository.RequestedServiceRepository;
import com.webtech.homeservicesapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HousingService {

    @Autowired
    private HousingRepository housingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestedServiceRepository serviceRepository;

    public ResponseEntity<?> addHousingWithServices(HousingWithServicesDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId()).orElse(null);
        if (owner == null) return ResponseEntity.badRequest().body("Invalid owner ID");

        Housing housing = new Housing();
        housing.setTitle(dto.getTitle());
        housing.setDescription(dto.getDescription());
        housing.setLocation(dto.getLocation());
        String p1 = dto.getPhoto_1();
        String p2 = dto.getPhoto_2();
        String p3 = dto.getPhoto_3();

        System.out.println("Original photo_1 path: " + p1);
        String finalPhoto1 = p1 == null ? "/images/logo.png" : (p1.startsWith("/uploads/") ? p1 : "/uploads/" + p1);
        System.out.println("Final photo_1 path: " + finalPhoto1);
        housing.setPhoto_1(finalPhoto1);
        housing.setPhoto_2(p2 == null ? null : (p2.startsWith("/uploads/") ? p2 : "/uploads/" + p2));
        housing.setPhoto_3(p3 == null ? null : (p3.startsWith("/uploads/") ? p3 : "/uploads/" + p3));

        housing.setConstraint_text(dto.getConstraint_text());
        housing.setOwner(owner);
        housing.setAmenities(dto.getAmenities());
        
        // Set maxGuests with a default value if null
        Integer maxGuests = dto.getMaxGuests();
        if (maxGuests == null) {
            // Extract max guests from constraint_text if available
            String constraintText = dto.getConstraint_text();
            if (constraintText != null && constraintText.toLowerCase().contains("max")) {
                try {
                    String[] parts = constraintText.split("\\s+");
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].equalsIgnoreCase("max") && i + 2 < parts.length) {
                            maxGuests = Integer.parseInt(parts[i + 1]);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    maxGuests = 2; // Default value if parsing fails
                }
            } else {
                maxGuests = 2; // Default value if no constraint text or parsing fails
            }
        }
        housing.setMaxGuests(maxGuests);

        List<RequestedService> services = new ArrayList<>();
        for (String title : dto.getServices()) {
            RequestedService s = new RequestedService();
            s.setTitle(title);
            s.setHousing(housing);
            services.add(s);
        }
        housing.setServices(services);

        housingRepository.save(housing);
        return ResponseEntity.ok("Housing added successfully with services");
    }

    public List<HousingDTO> searchByLocation(String location) {
        List<Housing> houses = housingRepository.findByLocationContainingIgnoreCase(location);
        return convertToDTOs(houses);
    }

    public List<HousingDTO> searchByFilters(String location, String checkIn, String checkOut, Integer person) {
        List<Housing> houses = (location == null || location.trim().isEmpty())
                ? housingRepository.findAll()
                : housingRepository.findByLocationContainingIgnoreCase(location);

        return houses.stream()
                .map(h -> new HousingDTO(
                        h.getHousing_id(),
                        h.getTitle(),
                        h.getDescription(),
                        h.getLocation(),
                        h.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    public List<HousingDTO> getByOwnerId(Long ownerId) {
        List<Housing> housings = housingRepository.findByOwnerId(ownerId);
        return housings.stream().map(h -> new HousingDTO(
                h.getHousing_id(),
                h.getTitle(),
                h.getDescription(),
                h.getLocation(),
                h.getImageUrl()
        )).collect(Collectors.toList());
    }

    public HousingWithServicesDTO getHousingWithServices(Long id) {
        Housing h = housingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No housing found with id " + id));

        HousingWithServicesDTO dto = new HousingWithServicesDTO();
        dto.setTitle(h.getTitle());
        dto.setDescription(h.getDescription());
        dto.setLocation(h.getLocation());
        dto.setPhoto_1(h.getPhoto_1());
        dto.setPhoto_2(h.getPhoto_2());
        dto.setPhoto_3(h.getPhoto_3());
        dto.setConstraint_text(h.getConstraint_text());
        dto.setOwnerId(h.getOwner().getId());
        dto.setAmenities(h.getAmenities());
        dto.setMaxGuests(h.getMaxGuests());
        dto.setServices(
            h.getServices()
             .stream()
             .map(RequestedService::getTitle)
             .collect(Collectors.toList())
        );
        return dto;
    }

    public ResponseEntity<?> updateHousing(Long id, HousingWithServicesDTO dto) {
        Optional<Housing> optionalHousing = housingRepository.findById(id);
        if (optionalHousing.isEmpty()) return ResponseEntity.notFound().build();

        Housing housing = optionalHousing.get();
        housing.setTitle(dto.getTitle());
        housing.setDescription(dto.getDescription());
        housing.setLocation(dto.getLocation());
        housing.setPhoto_1(dto.getPhoto_1());
        housing.setPhoto_2(dto.getPhoto_2());
        housing.setPhoto_3(dto.getPhoto_3());
        housing.setConstraint_text(dto.getConstraint_text());
        housing.setMaxGuests(dto.getMaxGuests());

        serviceRepository.deleteAll(housing.getServices());

        List<RequestedService> updatedServices = new ArrayList<>();
        for (String title : dto.getServices()) {
            RequestedService s = new RequestedService();
            s.setTitle(title);
            s.setHousing(housing);
            updatedServices.add(s);
        }
        housing.setServices(updatedServices);

        housingRepository.save(housing);
        return ResponseEntity.ok("Housing updated successfully");
    }

    public ResponseEntity<?> deleteHousing(Long id) {
        if (!housingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        housingRepository.deleteById(id);
        return ResponseEntity.ok("Listing deleted successfully");
    }

    private List<HousingDTO> convertToDTOs(List<Housing> houses) {
        return houses.stream()
                .map(h -> new HousingDTO(
                        h.getHousing_id(),
                        h.getTitle(),
                        h.getDescription(),
                        h.getLocation(),
                        h.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
