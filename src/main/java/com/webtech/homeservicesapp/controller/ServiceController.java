package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.RequestedService;
import com.webtech.homeservicesapp.repository.HousingRepository;
import com.webtech.homeservicesapp.repository.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@CrossOrigin("*")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private HousingRepository housingRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addServiceToHousing(@RequestBody RequestedService serviceData) {
        Long housingId = serviceData.getHousing().getId();
        Housing housing = housingRepository.findById(housingId).orElse(null);

        if (housing == null) {
            return ResponseEntity.badRequest().body("Housing not found");
        }

        serviceData.setHousing(housing);
        RequestedService saved = serviceRepository.save(serviceData);

        return ResponseEntity.ok(saved);
    }
}
