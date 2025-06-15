package com.domuswap.repository;

import com.domuswap.model.RequestedService;
import com.domuswap.model.User;
import com.domuswap.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestedServiceRepository extends JpaRepository<RequestedService, Long> {
    List<RequestedService> findByUser(User user);
    List<RequestedService> findByHousing(Housing housing);
} 