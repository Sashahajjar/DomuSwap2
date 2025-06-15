package com.domuswap.repository;

import com.domuswap.model.RequestedService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<RequestedService, Long> {
    // You can add custom queries later if needed
} 