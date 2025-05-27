package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HousingRepository extends JpaRepository<Housing, Long> {
    List<Housing> findByLocationContainingIgnoreCase(String location);
    List<Housing> findByOwnerId(Long ownerId);
}
