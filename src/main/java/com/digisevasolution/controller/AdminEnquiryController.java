package com.digisevasolution.controller;

import com.digisevasolution.dto.request.UpdateEnquiryStatusRequest;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.EnquiryResponse;
import com.digisevasolution.entity.EnquiryStatus;
import com.digisevasolution.service.EnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/enquiries")
@Tag(name = "Admin Customer Enquiries", description = "Management dashboard for tracking visitor inquiries and status lifecycle (JWT required)")
public class AdminEnquiryController {

    private final EnquiryService enquiryService;

    public AdminEnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @GetMapping
    @Operation(summary = "List Customer Enquiries", description = "Get paginated listing of customer inquiries with optional status filter (NEW, CONTACTED, RESOLVED).")
    public ResponseEntity<ApiResponse<Page<EnquiryResponse>>> getAllEnquiriesAdmin(
            @RequestParam(required = false) EnquiryStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EnquiryResponse> enquiriesPage = enquiryService.getAllEnquiriesAdmin(status, pageable);
        ApiResponse<Page<EnquiryResponse>> response = ApiResponse.success("Enquiries fetched successfully", enquiriesPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Enquiry Detail", description = "Fetch single customer inquiry details by ID.")
    public ResponseEntity<ApiResponse<EnquiryResponse>> getEnquiryByIdAdmin(@PathVariable Long id) {
        EnquiryResponse enquiry = enquiryService.getEnquiryByIdAdmin(id);
        ApiResponse<EnquiryResponse> response = ApiResponse.success("Enquiry detail fetched successfully", enquiry);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update Enquiry Status", description = "Update customer inquiry resolution lifecycle status (NEW -> CONTACTED -> RESOLVED).")
    public ResponseEntity<ApiResponse<EnquiryResponse>> updateEnquiryStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEnquiryStatusRequest request) {

        EnquiryResponse updated = enquiryService.updateEnquiryStatus(id, request);
        ApiResponse<EnquiryResponse> response = ApiResponse.success("Enquiry status updated successfully", updated);
        return ResponseEntity.ok(response);
    }
}
