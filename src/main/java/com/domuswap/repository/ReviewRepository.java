package com.domuswap.repository;

import com.domuswap.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.housing.housing_id = :housingId")
    List<Review> findByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT (AVG(r.cleanlinessRating) + AVG(r.locationRating) + AVG(r.checkinExperienceRating) + AVG(r.valueForMoneyRating)) / 4.0 FROM Review r WHERE r.housing.housing_id = :housingId")
    Double getAverageRatingByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.housing.housing_id = :housingId AND r.user.id = :userId")
    boolean existsByHousingIdAndUserId(@Param("housingId") Long housingId, @Param("userId") Long userId);
    @Query("SELECT AVG(r.cleanlinessRating) FROM Review r WHERE r.housing.housing_id = :housingId")
    Double getAverageCleanlinessByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT AVG(r.locationRating) FROM Review r WHERE r.housing.housing_id = :housingId")
    Double getAverageLocationByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT AVG(r.checkinExperienceRating) FROM Review r WHERE r.housing.housing_id = :housingId")
    Double getAverageCheckinExperienceByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT AVG(r.valueForMoneyRating) FROM Review r WHERE r.housing.housing_id = :housingId")
    Double getAverageValueForMoneyByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT AVG((r.cleanlinessRating + r.locationRating + r.checkinExperienceRating + r.valueForMoneyRating) / 4.0) FROM Review r WHERE r.housing.owner.id = :ownerId")
    Double getAverageRatingByOwnerId(@Param("ownerId") Long ownerId);
} 