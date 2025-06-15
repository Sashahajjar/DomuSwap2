package com.domuswap.controller;

import com.domuswap.model.RequestedService;
import com.domuswap.model.User;
import com.domuswap.model.Housing;
import com.domuswap.repository.RequestedServiceRepository;
import com.domuswap.repository.UserRepository;
import com.domuswap.repository.HousingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/services")
public class ServiceController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private RequestedServiceRepository requestedServiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HousingRepository housingRepository;

    @PostMapping("/request")
    @ResponseBody
    public ResponseEntity<?> requestService(@RequestBody Map<String, Object> payload, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to request services");
        }

        try {
            Long housingId = Long.valueOf(payload.get("housingId").toString());
            String serviceType = (String) payload.get("serviceType");
            String description = (String) payload.get("description");
            Double price = Double.valueOf(payload.get("price").toString());

            Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new RuntimeException("Housing not found"));

            RequestedService service = new RequestedService();
            service.setUser(loggedInUser);
            service.setHousing(housing);
            service.setServiceType(serviceType);
            service.setDescription(description);
            service.setPrice(price);
            service.setStatus("PENDING");
            service.setCreatedAt(LocalDateTime.now());

            requestedServiceRepository.save(service);
            return ResponseEntity.ok("Service request submitted successfully");
        } catch (Exception e) {
            logger.error("Error requesting service", e);
            return ResponseEntity.badRequest().body("Error requesting service: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<?> getUserServices(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to view services");
        }

        try {
            List<RequestedService> services = requestedServiceRepository.findByUser(loggedInUser);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error retrieving user services", e);
            return ResponseEntity.badRequest().body("Error retrieving services: " + e.getMessage());
        }
    }

    @GetMapping("/housing/{housingId}")
    @ResponseBody
    public ResponseEntity<?> getHousingServices(@PathVariable Long housingId) {
        try {
            Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new RuntimeException("Housing not found"));

            List<RequestedService> services = requestedServiceRepository.findByHousing(housing);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error retrieving housing services", e);
            return ResponseEntity.badRequest().body("Error retrieving services: " + e.getMessage());
        }
    }

    @PostMapping("/update-status/{serviceId}")
    @ResponseBody
    public ResponseEntity<?> updateServiceStatus(
            @PathVariable Long serviceId,
            @RequestBody Map<String, String> payload,
            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to update service status");
        }

        try {
            String newStatus = payload.get("status");
            RequestedService service = requestedServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

            if (!service.getHousing().getOwner().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.badRequest().body("Only the housing owner can update service status");
            }

            service.setStatus(newStatus);
            requestedServiceRepository.save(service);
            return ResponseEntity.ok("Service status updated successfully");
        } catch (Exception e) {
            logger.error("Error updating service status", e);
            return ResponseEntity.badRequest().body("Error updating service status: " + e.getMessage());
        }
    }
} 