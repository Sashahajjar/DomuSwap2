package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.model.Review;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.repository.HousingRepository;
import com.webtech.homeservicesapp.repository.ReservationRepository;
import com.webtech.homeservicesapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ListingController {

    @Autowired
    private HousingRepository housingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/listing/{id}")
    public String showListing(@PathVariable Long id, Model model, HttpSession session) {
        return housingRepository.findById(id)
            .map(housing -> {
                // Get reviews and average rating
                List<Review> reviews = reviewRepository.findByHousingId(id);
                Double averageRating = reviewRepository.getAverageRatingByHousingId(id);
                Double averageCleanliness = reviewRepository.getAverageCleanlinessByHousingId(id);
                Double averageLocation = reviewRepository.getAverageLocationByHousingId(id);
                Double averageCheckin = reviewRepository.getAverageCheckinExperienceByHousingId(id);
                Double averageValue = reviewRepository.getAverageValueForMoneyByHousingId(id);
                
                // Check if current user can leave a review
                boolean canLeaveReview = false;
                User loggedInUser = (User) session.getAttribute("loggedInUser");
                if (loggedInUser != null) {
                    canLeaveReview = reservationRepository.existsByHousingIdAndUserId(id, loggedInUser.getId()) &&
                                   !reviewRepository.existsByHousingIdAndUserId(id, loggedInUser.getId());
                }

                model.addAttribute("housing", housing);
                model.addAttribute("reviews", reviews);
                model.addAttribute("averageRating", averageRating != null ? averageRating : 0.0);
                model.addAttribute("averageCleanliness", averageCleanliness != null ? averageCleanliness : 0.0);
                model.addAttribute("averageLocation", averageLocation != null ? averageLocation : 0.0);
                model.addAttribute("averageCheckin", averageCheckin != null ? averageCheckin : 0.0);
                model.addAttribute("averageValue", averageValue != null ? averageValue : 0.0);
                model.addAttribute("canLeaveReview", canLeaveReview);
                return "listing";
            })
            .orElse("error");
    }
}