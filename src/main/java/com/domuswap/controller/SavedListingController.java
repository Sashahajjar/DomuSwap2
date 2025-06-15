package com.domuswap.controller;

import com.domuswap.model.SavedListing;
import com.domuswap.model.User;
import com.domuswap.model.Housing;
import com.domuswap.repository.SavedListingRepository;
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
@RequestMapping("/api/saved-listings")
public class SavedListingController {
    private static final Logger logger = LoggerFactory.getLogger(SavedListingController.class);

    @Autowired
    private SavedListingRepository savedListingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HousingRepository housingRepository;

    @PostMapping("/save/{housingId}")
    @ResponseBody
    public ResponseEntity<?> saveListing(@PathVariable Long housingId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to save listings");
        }

        try {
            Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new RuntimeException("Housing not found"));

            if (savedListingRepository.existsByUserAndHousing(loggedInUser, housing)) {
                return ResponseEntity.badRequest().body("Listing already saved");
            }

            SavedListing savedListing = new SavedListing();
            savedListing.setUser(loggedInUser);
            savedListing.setHousing(housing);
            savedListing.setSavedAt(LocalDateTime.now());

            savedListingRepository.save(savedListing);
            return ResponseEntity.ok("Listing saved successfully");
        } catch (Exception e) {
            logger.error("Error saving listing", e);
            return ResponseEntity.badRequest().body("Error saving listing: " + e.getMessage());
        }
    }

    @DeleteMapping("/unsave/{housingId}")
    @ResponseBody
    public ResponseEntity<?> unsaveListing(@PathVariable Long housingId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to unsave listings");
        }

        try {
            Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new RuntimeException("Housing not found"));

            SavedListing savedListing = savedListingRepository.findByUserAndHousing(loggedInUser, housing)
                .orElseThrow(() -> new RuntimeException("Saved listing not found"));

            savedListingRepository.delete(savedListing);
            return ResponseEntity.ok("Listing unsaved successfully");
        } catch (Exception e) {
            logger.error("Error unsaving listing", e);
            return ResponseEntity.badRequest().body("Error unsaving listing: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<?> getUserSavedListings(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to view saved listings");
        }

        try {
            List<SavedListing> savedListings = savedListingRepository.findByUser(loggedInUser);
            return ResponseEntity.ok(savedListings);
        } catch (Exception e) {
            logger.error("Error retrieving saved listings", e);
            return ResponseEntity.badRequest().body("Error retrieving saved listings: " + e.getMessage());
        }
    }

    @GetMapping("/check/{housingId}")
    @ResponseBody
    public ResponseEntity<?> checkIfSaved(@PathVariable Long housingId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to check saved status");
        }

        try {
            Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new RuntimeException("Housing not found"));

            boolean isSaved = savedListingRepository.existsByUserAndHousing(loggedInUser, housing);
            return ResponseEntity.ok(Map.of("isSaved", isSaved));
        } catch (Exception e) {
            logger.error("Error checking saved status", e);
            return ResponseEntity.badRequest().body("Error checking saved status: " + e.getMessage());
        }
    }
} 