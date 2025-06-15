package com.domuswap.repository;

import com.domuswap.model.OwnerReview;
import com.domuswap.model.Reservation;
import com.domuswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {
    
    boolean existsByReservationAndOwner(Reservation reservation, User owner);
    
    @Query("SELECT AVG(r.rating) FROM OwnerReview r WHERE r.customer.id = :customerId")
    Double findAverageRatingByCustomerId(@Param("customerId") Long customerId);
} 