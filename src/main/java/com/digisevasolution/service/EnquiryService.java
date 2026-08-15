package com.digisevasolution.service;

import com.digisevasolution.dto.request.EnquiryRequest;
import com.digisevasolution.dto.request.UpdateEnquiryStatusRequest;
import com.digisevasolution.dto.response.EnquiryResponse;
import com.digisevasolution.entity.EnquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnquiryService {
    EnquiryResponse createEnquiry(EnquiryRequest request);
    Page<EnquiryResponse> getAllEnquiriesAdmin(EnquiryStatus status, Pageable pageable);
    EnquiryResponse getEnquiryByIdAdmin(Long id);
    EnquiryResponse updateEnquiryStatus(Long id, UpdateEnquiryStatusRequest request);
}
