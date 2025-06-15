package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.Review;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.model.OwnerReview;
import com.webtech.homeservicesapp.model.Reservation;
import com.webtech.homeservicesapp.repository.HousingRepository;
import com.webtech.homeservicesapp.repository.ReservationRepository;
import com.webtech.homeservicesapp.repository.ReviewRepository;
import com.webtech.homeservicesapp.repository.OwnerReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HousingRepository housingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OwnerReviewRepository ownerReviewRepository;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addReview(
            @RequestBody Map<String, Object> payload,
            HttpSession session) {
        
        logger.info("Received review submission request: {}", payload);
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warn("Review submission failed: User not logged in");
            return ResponseEntity.badRequest().body("User must be logged in to leave a review");
        }

        try {
            Long housingId = Long.valueOf(payload.get("housingId").toString());
            Integer cleanlinessRating = Integer.valueOf(payload.get("cleanlinessRating").toString());
            Integer locationRating = Integer.valueOf(payload.get("locationRating").toString());
            Integer checkinExperienceRating = Integer.valueOf(payload.get("checkinExperienceRating").toString());
            Integer valueForMoneyRating = Integer.valueOf(payload.get("valueForMoneyRating").toString());
            String comment = (String) payload.get("comment");

            logger.info("Processing review for housing {} by user {}", housingId, loggedInUser.getId());

            // Check if user has booked this housing
            if (!reservationRepository.existsByHousingIdAndUserId(housingId, loggedInUser.getId())) {
                logger.warn("Review submission failed: User {} has not booked housing {}", loggedInUser.getId(), housingId);
                return ResponseEntity.badRequest().body("You must have booked this housing to leave a review");
            }

            // Check if user has already reviewed this housing
            if (reviewRepository.existsByHousingIdAndUserId(housingId, loggedInUser.getId())) {
                logger.warn("Review submission failed: User {} has already reviewed housing {}", loggedInUser.getId(), housingId);
                return ResponseEntity.badRequest().body("You have already reviewed this housing");
            }

            Housing housing = housingRepository.findById(housingId)
                    .orElse(null);
            if (housing == null) {
                logger.warn("Review submission failed: Housing {} not found", housingId);
                return ResponseEntity.badRequest().body("Housing not found");
            }

            Review review = new Review();
            review.setCleanlinessRating(cleanlinessRating);
            review.setLocationRating(locationRating);
            review.setCheckinExperienceRating(checkinExperienceRating);
            review.setValueForMoneyRating(valueForMoneyRating);
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());
            review.setHousing(housing);
            review.setUser(loggedInUser);

            reviewRepository.save(review);
            logger.info("Review successfully saved for housing {} by user {}", housingId, loggedInUser.getId());
            return ResponseEntity.ok("Review added successfully");
        } catch (Exception e) {
            logger.error("Error processing review submission", e);
            return ResponseEntity.badRequest().body("Error processing review: " + e.getMessage());
        }
    }

    @PostMapping("/customer")
    @ResponseBody
    public ResponseEntity<?> addOwnerReview(@RequestBody Map<String, Object> payload, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to leave a review");
        }
        try {
            Long reservationId = Long.valueOf(payload.get("reservationId").toString());
            Long customerId = Long.valueOf(payload.get("customerId").toString());
            Integer rating = Integer.valueOf(payload.get("rating").toString());

            Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                return ResponseEntity.badRequest().body("Reservation not found");
            }
            User customer = reservation.getUser();
            if (!customer.getId().equals(customerId)) {
                return ResponseEntity.badRequest().body("Customer does not match reservation");
            }
            User owner = reservation.getHousing().getOwner();
            if (!owner.getId().equals(loggedInUser.getId())) {
                return ResponseEntity.badRequest().body("Only the owner can leave a review for this reservation");
            }
            // Block duplicate reviews
            if (ownerReviewRepository.existsByReservationAndOwner(reservation, owner)) {
                return ResponseEntity.badRequest().body("You have already reviewed this customer for this reservation");
            }
            OwnerReview review = new OwnerReview();
            review.setReservation(reservation);
            review.setOwner(owner);
            review.setCustomer(customer);
            review.setRating(rating);
            review.setCreatedAt(java.time.LocalDateTime.now());
            ownerReviewRepository.save(review);
            return ResponseEntity.ok("Review added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing review: " + e.getMessage());
        }
    }

    @GetMapping("/customer/average")
    @ResponseBody
    public ResponseEntity<?> getCustomerAverageRating(@RequestParam Long userId) {
        try {
            Double averageRating = ownerReviewRepository.getAverageRatingByCustomerId(userId);
            if (averageRating == null) {
                averageRating = 0.0;
            }
            return ResponseEntity.ok(Map.of("averageRating", averageRating));
        } catch (Exception e) {
            logger.error("Error getting customer average rating", e);
            return ResponseEntity.badRequest().body("Error getting average rating: " + e.getMessage());
        }
    }

    @GetMapping("/owner/average")
    @ResponseBody
    public ResponseEntity<?> getOwnerAverageRating(@RequestParam Long userId) {
        try {
            Double averageRating = reviewRepository.getAverageRatingByOwnerId(userId);
            if (averageRating == null) {
                averageRating = 0.0;
            }
            return ResponseEntity.ok(Map.of("averageRating", averageRating));
        } catch (Exception e) {
            logger.error("Error getting owner average rating", e);
            return ResponseEntity.badRequest().body("Error getting average rating: " + e.getMessage());
        }
    }
} 