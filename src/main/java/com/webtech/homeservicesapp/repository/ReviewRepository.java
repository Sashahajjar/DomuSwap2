package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.housing.housing_id = :housingId")
    List<Review> findByHousingId(@Param("housingId") Long housingId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.housing.housing_id = :housingId")
    Double getAverageRatingByHousingId(@Param("housingId") Long housingId);
    
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.housing.housing_id = :housingId AND r.user.id = :userId")
    boolean existsByHousingIdAndUserId(@Param("housingId") Long housingId, @Param("userId") Long userId);
} 