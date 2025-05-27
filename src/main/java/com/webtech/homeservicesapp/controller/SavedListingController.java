package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.Reservation;
import com.webtech.homeservicesapp.model.SavedListing;
import com.webtech.homeservicesapp.repository.HousingRepository;
import com.webtech.homeservicesapp.repository.SavedListingRepository;
import com.webtech.homeservicesapp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SavedListingController {

    @Autowired
    private SavedListingRepository savedListingRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private HousingRepository housingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/saved_listings")
    public String showSavedListings(@RequestParam Long userId, Model model) {
        // Get saved listings with housing data eagerly loaded
        List<SavedListing> savedListings = entityManager.createQuery(
            "SELECT sl FROM SavedListing sl JOIN FETCH sl.housing WHERE sl.userId = :userId", 
            SavedListing.class)
            .setParameter("userId", userId)
            .getResultList();
        
        List<Housing> savedHousings = savedListings.stream()
            .map(SavedListing::getHousing)
            .collect(Collectors.toList());
        
        // Get reservations
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        
        model.addAttribute("savedListings", savedHousings);
        model.addAttribute("reservations", reservations);
        model.addAttribute("userId", userId);
        
        return "saved_listings";
    }

    @PostMapping("/api/saved-listings")
    @ResponseBody
    public ResponseEntity<?> saveListing(@RequestParam Long userId, @RequestParam Long housingId) {
        SavedListing savedListing = new SavedListing();
        savedListing.setUserId(userId);
        savedListing.setHousing(housingRepository.findById(housingId).orElse(null));
        savedListingRepository.save(savedListing);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/saved-listings/{id}")
    @ResponseBody
    public ResponseEntity<?> removeSavedListing(@PathVariable Long id) {
        savedListingRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 