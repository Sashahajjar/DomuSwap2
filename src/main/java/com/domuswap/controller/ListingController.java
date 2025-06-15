package com.domuswap.controller;

import com.domuswap.model.Housing;
import com.domuswap.model.User;
import com.domuswap.repository.HousingRepository;
import com.domuswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/listings")
public class ListingController {
    private static final Logger logger = LoggerFactory.getLogger(ListingController.class);

    @Autowired
    private HousingRepository housingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> searchListings(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) String amenities) {
        try {
            List<Housing> listings = housingRepository.searchListings(
                location, minPrice, maxPrice, minBedrooms, maxBedrooms, amenities);
            return ResponseEntity.ok(listings);
        } catch (Exception e) {
            logger.error("Error searching listings", e);
            return ResponseEntity.badRequest().body("Error searching listings: " + e.getMessage());
        }
    }

    @GetMapping("/featured")
    @ResponseBody
    public ResponseEntity<?> getFeaturedListings() {
        try {
            List<Housing> featuredListings = housingRepository.findFeaturedListings();
            return ResponseEntity.ok(featuredListings);
        } catch (Exception e) {
            logger.error("Error retrieving featured listings", e);
            return ResponseEntity.badRequest().body("Error retrieving featured listings: " + e.getMessage());
        }
    }

    @GetMapping("/recent")
    @ResponseBody
    public ResponseEntity<?> getRecentListings() {
        try {
            List<Housing> recentListings = housingRepository.findRecentListings();
            return ResponseEntity.ok(recentListings);
        } catch (Exception e) {
            logger.error("Error retrieving recent listings", e);
            return ResponseEntity.badRequest().body("Error retrieving recent listings: " + e.getMessage());
        }
    }

    @GetMapping("/similar/{listingId}")
    @ResponseBody
    public ResponseEntity<?> getSimilarListings(@PathVariable Long listingId) {
        try {
            Housing listing = housingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

            List<Housing> similarListings = housingRepository.findSimilarListings(
                listing.getLocation(),
                listing.getPrice(),
                listing.getBedrooms(),
                listingId
            );
            return ResponseEntity.ok(similarListings);
        } catch (Exception e) {
            logger.error("Error retrieving similar listings", e);
            return ResponseEntity.badRequest().body("Error retrieving similar listings: " + e.getMessage());
        }
    }

    @GetMapping("/owner")
    @ResponseBody
    public ResponseEntity<?> getOwnerListings(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to view owner listings");
        }

        try {
            List<Housing> ownerListings = housingRepository.findByOwner(loggedInUser);
            return ResponseEntity.ok(ownerListings);
        } catch (Exception e) {
            logger.error("Error retrieving owner listings", e);
            return ResponseEntity.badRequest().body("Error retrieving owner listings: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<?> getListingStats() {
        try {
            Map<String, Object> stats = Map.of(
                "totalListings", housingRepository.count(),
                "averagePrice", housingRepository.getAveragePrice(),
                "popularLocations", housingRepository.getPopularLocations()
            );
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error retrieving listing stats", e);
            return ResponseEntity.badRequest().body("Error retrieving listing stats: " + e.getMessage());
        }
    }
} 