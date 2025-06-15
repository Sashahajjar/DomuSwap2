package com.domuswap.service;

import com.domuswap.model.RequestedService;
import com.domuswap.repository.RequestedServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RequestedServiceService {
    @Autowired
    private RequestedServiceRepository requestedServiceRepository;
    public List<RequestedService> getAllRequestedServices() {
        return requestedServiceRepository.findAll();
    }
    public RequestedService saveRequestedService(RequestedService requestedService) {
        return requestedServiceRepository.save(requestedService);
    }
    public void deleteRequestedService(Long id) {
        requestedServiceRepository.deleteById(id);
    }
} 