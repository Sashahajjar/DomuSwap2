package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.Review;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.repository.HousingRepository;
import com.webtech.homeservicesapp.repository.ReservationRepository;
import com.webtech.homeservicesapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HousingRepository housingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addReview(
            @RequestParam Long housingId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            HttpSession session) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to leave a review");
        }

        // Check if user has booked this housing
        if (!reservationRepository.existsByHousingIdAndUserId(housingId, loggedInUser.getId())) {
            return ResponseEntity.badRequest().body("You must have booked this housing to leave a review");
        }

        // Check if user has already reviewed this housing
        if (reviewRepository.existsByHousingIdAndUserId(housingId, loggedInUser.getId())) {
            return ResponseEntity.badRequest().body("You have already reviewed this housing");
        }

        Housing housing = housingRepository.findById(housingId)
                .orElse(null);
        if (housing == null) {
            return ResponseEntity.badRequest().body("Housing not found");
        }

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setHousing(housing);
        review.setUser(loggedInUser);

        reviewRepository.save(review);
        return ResponseEntity.ok("Review added successfully");
    }
} 