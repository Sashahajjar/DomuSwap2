package com.domuswap.service;

import com.domuswap.model.Review;
import com.domuswap.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    public List<Review> getReviewsByHousingId(Long housingId) {
        return reviewRepository.findByHousingId(housingId);
    }
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
} 