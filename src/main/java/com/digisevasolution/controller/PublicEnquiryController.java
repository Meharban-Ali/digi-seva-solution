package com.digisevasolution.controller;

import com.digisevasolution.dto.request.EnquiryRequest;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.EnquiryResponse;
import com.digisevasolution.service.EnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enquiries")
@Tag(name = "Public Customer Enquiry", description = "Public submission form for visitor inquiries with phone rate-limiting")
public class PublicEnquiryController {

    private final EnquiryService enquiryService;

    public PublicEnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping
    @Operation(summary = "Submit Customer Enquiry", description = "Submit a public inquiry (name, phone, optional email, optional service ID, message). Rate limited to 5 submissions per phone/hour.")
    public ResponseEntity<ApiResponse<EnquiryResponse>> createEnquiry(
            @Valid @RequestBody EnquiryRequest request) {
        EnquiryResponse created = enquiryService.createEnquiry(request);
        ApiResponse<EnquiryResponse> response = ApiResponse.success("Enquiry submitted successfully", created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
