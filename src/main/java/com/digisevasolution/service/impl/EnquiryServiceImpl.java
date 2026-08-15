package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.EnquiryRequest;
import com.digisevasolution.dto.request.UpdateEnquiryStatusRequest;
import com.digisevasolution.dto.response.EnquiryResponse;
import com.digisevasolution.entity.Enquiry;
import com.digisevasolution.entity.EnquiryStatus;
import com.digisevasolution.exception.ApiException;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.EnquiryRepository;
import com.digisevasolution.service.EnquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private static final int MAX_ENQUIRIES_PER_HOUR = 5;

    private final EnquiryRepository enquiryRepository;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    @Transactional
    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        // Enforce rate limiting: maximum 5 enquiries per phone number per hour
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentCount = enquiryRepository.countByPhoneAndCreatedAtAfter(request.getPhone(), oneHourAgo);

        if (recentCount >= MAX_ENQUIRIES_PER_HOUR) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,
                    "Enquiry submission rate limit exceeded. Maximum 5 enquiries allowed per phone number per hour.");
        }

        Enquiry enquiry = new Enquiry(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                request.getServiceId(),
                request.getMessage(),
                EnquiryStatus.NEW
        );

        Enquiry saved = enquiryRepository.save(enquiry);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnquiryResponse> getAllEnquiriesAdmin(EnquiryStatus status, Pageable pageable) {
        Page<Enquiry> page;
        if (status != null) {
            page = enquiryRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        } else {
            page = enquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        return page.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EnquiryResponse getEnquiryByIdAdmin(Long id) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry", "id", id));
        return mapToResponse(enquiry);
    }

    @Override
    @Transactional
    public EnquiryResponse updateEnquiryStatus(Long id, UpdateEnquiryStatusRequest request) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry", "id", id));

        if (request.getStatus() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Enquiry status cannot be null.");
        }

        enquiry.setStatus(request.getStatus());
        Enquiry updated = enquiryRepository.save(enquiry);
        return mapToResponse(updated);
    }

    private EnquiryResponse mapToResponse(Enquiry enquiry) {
        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getName(),
                enquiry.getPhone(),
                enquiry.getEmail(),
                enquiry.getServiceId(),
                enquiry.getMessage(),
                enquiry.getStatus(),
                enquiry.getCreatedAt(),
                enquiry.getUpdatedAt()
        );
    }
}
