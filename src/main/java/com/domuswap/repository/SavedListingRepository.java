package com.domuswap.repository;

import com.domuswap.model.SavedListing;
import com.domuswap.model.User;
import com.domuswap.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedListingRepository extends JpaRepository<SavedListing, Long> {
    boolean existsByUserAndHousing(User user, Housing housing);
    Optional<SavedListing> findByUserAndHousing(User user, Housing housing);
    List<SavedListing> findByUser(User user);
    @Modifying
    @Query("DELETE FROM SavedListing sl WHERE sl.userId = :userId AND sl.housing.housing_id = :housingId")
    void deleteByUserIdAndHousingId(@Param("userId") Long userId, @Param("housingId") Long housingId);
} 