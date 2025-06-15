package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.OwnerReview;
import com.webtech.homeservicesapp.model.Reservation;
import com.webtech.homeservicesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {
    boolean existsByReservationAndOwner(Reservation reservation, User owner);
    
    @Query("SELECT AVG(r.rating) FROM OwnerReview r WHERE r.customer.id = :customerId")
    Double getAverageRatingByCustomerId(@Param("customerId") Long customerId);
} 