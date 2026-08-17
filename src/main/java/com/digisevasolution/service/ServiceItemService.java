package com.digisevasolution.service;

import com.digisevasolution.dto.request.ServiceItemRequest;
import com.digisevasolution.dto.response.PublicServiceResponse;
import com.digisevasolution.dto.response.ServiceItemResponse;
import com.digisevasolution.entity.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceItemService {
    // Admin operations
    ServiceItemResponse createService(ServiceItemRequest request);
    ServiceItemResponse updateService(Long id, ServiceItemRequest request);
    void softDeleteService(Long id);
    Page<ServiceItemResponse> getAllServicesAdmin(Pageable pageable);
    ServiceItemResponse getServiceByIdAdmin(Long id);

    // Public operations
    List<PublicServiceResponse> getAllServicesPublic(String lang, ServiceCategory category, Boolean featured);
    PublicServiceResponse getServiceByIdPublic(Long id, String lang);
}
