package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.ServiceItemRequest;
import com.digisevasolution.dto.response.PublicServiceResponse;
import com.digisevasolution.dto.response.ServiceItemResponse;
import com.digisevasolution.entity.Category;
import com.digisevasolution.entity.DeliveryMode;
import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public ServiceItemServiceImpl(ServiceItemRepository serviceItemRepository, CategoryRepository categoryRepository) {
        this.serviceItemRepository = serviceItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ServiceItemResponse createService(ServiceItemRequest request) {
        ServiceItem serviceItem = new ServiceItem(
                request.getNameEn(),
                request.getNameHi(),
                request.getDescriptionEn(),
                request.getDescriptionHi(),
                request.getDeliveryMode(),
                request.getPrice(),
                request.getImageUrl(),
                request.getIsActive() != null ? request.getIsActive() : true,
                request.getIsFeatured() != null ? request.getIsFeatured() : false,
                request.getDisplayOrder() != null ? request.getDisplayOrder() : 0
        );

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            serviceItem.setCategory(category);
        }

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
        serviceItem.setDeliveryMode(request.getDeliveryMode());
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

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            serviceItem.setCategory(category);
        } else {
            serviceItem.setCategory(null);
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
    public List<PublicServiceResponse> getAllServicesPublic(String lang, DeliveryMode deliveryMode, Boolean featured) {
        return getAllServicesPublic(lang, deliveryMode, featured, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicServiceResponse> getAllServicesPublic(String lang, DeliveryMode deliveryMode, Boolean featured, Long categoryId, String categorySlug) {
        List<ServiceItem> services;

        if (categoryId != null) {
            if (deliveryMode != null) {
                services = serviceItemRepository.findByIsActiveTrueAndCategoryIdAndDeliveryModeOrderByDisplayOrderAscCreatedAtDesc(categoryId, deliveryMode);
            } else {
                services = serviceItemRepository.findByIsActiveTrueAndCategoryIdOrderByDisplayOrderAscCreatedAtDesc(categoryId);
            }
        } else if (categorySlug != null && !categorySlug.isBlank()) {
            if (deliveryMode != null) {
                services = serviceItemRepository.findByIsActiveTrueAndCategorySlugAndDeliveryModeOrderByDisplayOrderAscCreatedAtDesc(categorySlug, deliveryMode);
            } else {
                services = serviceItemRepository.findByIsActiveTrueAndCategorySlugOrderByDisplayOrderAscCreatedAtDesc(categorySlug);
            }
        } else if (Boolean.TRUE.equals(featured)) {
            if (deliveryMode != null) {
                services = serviceItemRepository.findByIsActiveTrueAndDeliveryModeAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc(deliveryMode);
            } else {
                services = serviceItemRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc();
            }
        } else {
            if (deliveryMode != null) {
                services = serviceItemRepository.findByIsActiveTrueAndDeliveryModeOrderByDisplayOrderAscCreatedAtDesc(deliveryMode);
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
        Category cat = service.getCategory();
        return new ServiceItemResponse(
                service.getId(),
                service.getNameEn(),
                service.getNameHi(),
                service.getDescriptionEn(),
                service.getDescriptionHi(),
                service.getDeliveryMode(),
                cat != null ? cat.getId() : null,
                cat != null ? cat.getNameEn() : null,
                cat != null ? cat.getNameHi() : null,
                cat != null ? cat.getSlug() : null,
                cat != null ? cat.getIcon() : null,
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

        Category cat = service.getCategory();
        String resolvedCatName = null;
        if (cat != null) {
            resolvedCatName = ("hi".equalsIgnoreCase(lang) && cat.getNameHi() != null && !cat.getNameHi().isBlank())
                    ? cat.getNameHi()
                    : cat.getNameEn();
        }

        return new PublicServiceResponse(
                service.getId(),
                resolvedName,
                resolvedDescription,
                service.getDeliveryMode(),
                cat != null ? cat.getId() : null,
                resolvedCatName,
                cat != null ? cat.getSlug() : null,
                cat != null ? cat.getIcon() : null,
                service.getPrice(),
                service.getImageUrl(),
                service.isFeatured(),
                service.getDisplayOrder()
        );
    }
}
