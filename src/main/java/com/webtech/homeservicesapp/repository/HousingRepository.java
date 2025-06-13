package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HousingRepository extends JpaRepository<Housing, Long> {
    List<Housing> findByLocationContainingIgnoreCase(String location);
    List<Housing> findByOwnerId(Long ownerId);

    @Query("SELECT h FROM Housing h LEFT JOIN FETCH h.services WHERE h.housing_id = :id")
    Optional<Housing> findByIdWithServices(@Param("id") Long id);
}
