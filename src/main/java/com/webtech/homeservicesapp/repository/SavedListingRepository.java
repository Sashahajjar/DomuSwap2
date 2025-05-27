package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.SavedListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedListingRepository extends JpaRepository<SavedListing, Long> {
    List<SavedListing> findByUserId(Long userId);
    
    @Modifying
    @Query("DELETE FROM SavedListing sl WHERE sl.userId = :userId AND sl.housing.housing_id = :housingId")
    void deleteByUserIdAndHousingId(@Param("userId") Long userId, @Param("housingId") Long housingId);
} 