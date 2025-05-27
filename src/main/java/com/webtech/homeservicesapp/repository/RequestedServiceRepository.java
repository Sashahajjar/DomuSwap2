package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.RequestedService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestedServiceRepository extends JpaRepository<RequestedService, Long> {
}
