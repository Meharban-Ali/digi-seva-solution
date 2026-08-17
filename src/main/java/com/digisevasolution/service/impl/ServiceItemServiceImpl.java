package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.ServiceItemRequest;
import com.digisevasolution.dto.response.PublicServiceResponse;
import com.digisevasolution.dto.response.ServiceItemResponse;
import com.digisevasolution.entity.ServiceCategory;
import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.ServiceItemRepository;
import com.digisevasolution.service.ServiceItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceItemServiceImpl implements ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;

    public ServiceItemServiceImpl(ServiceItemRepository serviceItemRepository) {
        this.serviceItemRepository = serviceItemRepository;
    }

    @Override
    @Transactional
    public ServiceItemResponse createService(ServiceItemRequest request) {
        ServiceItem serviceItem = new ServiceItem(
                request.getNameEn(),
                request.getNameHi(),
                request.getDescriptionEn(),
                request.getDescriptionHi(),
                request.getCategory(),
                request.getPrice(),
                request.getImageUrl(),
                request.getIsActive() != null ? request.getIsActive() : true,
                request.getIsFeatured() != null ? request.getIsFeatured() : false,
                request.getDisplayOrder() != null ? request.getDisplayOrder() : 0
        );

        ServiceItem saved = serviceItemRepository.save(serviceItem);
        return mapToAdminResponse(saved);
    }

    @Override
    @Transactional
    public ServiceItemResponse updateService(Long id, ServiceItemRequest request) {
        ServiceItem serviceItem = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceItem", "id", id));

        serviceItem.setNameEn(request.getNameEn());
        serviceItem.setNameHi(request.getNameHi());
        serviceItem.setDescriptionEn(request.getDescriptionEn());
        serviceItem.setDescriptionHi(request.getDescriptionHi());
        serviceItem.setCategory(request.getCategory());
        serviceItem.setPrice(request.getPrice());
        serviceItem.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) {
            serviceItem.setActive(request.getIsActive());
        }
        if (request.getIsFeatured() != null) {
            serviceItem.setFeatured(request.getIsFeatured());
        }
        if (request.getDisplayOrder() != null) {
            serviceItem.setDisplayOrder(request.getDisplayOrder());
        }

        ServiceItem updated = serviceItemRepository.save(serviceItem);
        return mapToAdminResponse(updated);
    }

    @Override
    @Transactional
    public void softDeleteService(Long id) {
        ServiceItem serviceItem = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceItem", "id", id));
        serviceItem.setActive(false);
        serviceItemRepository.save(serviceItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemResponse> getAllServicesAdmin(Pageable pageable) {
        return serviceItemRepository.findAll(pageable)
                .map(this::mapToAdminResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceItemResponse getServiceByIdAdmin(Long id) {
        ServiceItem serviceItem = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceItem", "id", id));
        return mapToAdminResponse(serviceItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicServiceResponse> getAllServicesPublic(String lang, ServiceCategory category, Boolean featured) {
        List<ServiceItem> services;
        if (Boolean.TRUE.equals(featured)) {
            if (category != null) {
                services = serviceItemRepository.findByIsActiveTrueAndCategoryAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc(category);
            } else {
                services = serviceItemRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc();
            }
        } else {
            if (category != null) {
                services = serviceItemRepository.findByIsActiveTrueAndCategoryOrderByDisplayOrderAscCreatedAtDesc(category);
            } else {
                services = serviceItemRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc();
            }
        }

        return services.stream()
                .map(service -> mapToPublicResponse(service, lang))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PublicServiceResponse getServiceByIdPublic(Long id, String lang) {
        ServiceItem serviceItem = serviceItemRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceItem", "id", id));
        return mapToPublicResponse(serviceItem, lang);
    }

    // Helper mapping for Admin full response
    private ServiceItemResponse mapToAdminResponse(ServiceItem service) {
        return new ServiceItemResponse(
                service.getId(),
                service.getNameEn(),
                service.getNameHi(),
                service.getDescriptionEn(),
                service.getDescriptionHi(),
                service.getCategory(),
                service.getPrice(),
                service.getImageUrl(),
                service.isActive(),
                service.isFeatured(),
                service.getDisplayOrder(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
    }

    // Helper mapping for Public single language view with Fallback logic
    private PublicServiceResponse mapToPublicResponse(ServiceItem service, String lang) {
        String resolvedName;
        String resolvedDescription;

        if ("hi".equalsIgnoreCase(lang)) {
            resolvedName = (service.getNameHi() != null && !service.getNameHi().isBlank())
                    ? service.getNameHi()
                    : service.getNameEn();
            resolvedDescription = (service.getDescriptionHi() != null && !service.getDescriptionHi().isBlank())
                    ? service.getDescriptionHi()
                    : service.getDescriptionEn();
        } else {
            resolvedName = service.getNameEn();
            resolvedDescription = service.getDescriptionEn();
        }

        return new PublicServiceResponse(
                service.getId(),
                resolvedName,
                resolvedDescription,
                service.getCategory(),
                service.getPrice(),
                service.getImageUrl(),
                service.isFeatured(),
                service.getDisplayOrder()
        );
    }
}
