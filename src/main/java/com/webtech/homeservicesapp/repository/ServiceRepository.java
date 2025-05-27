package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.RequestedService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<RequestedService, Long> {
    // You can add custom queries later if needed
}