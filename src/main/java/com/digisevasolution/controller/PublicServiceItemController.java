package com.digisevasolution.controller;

import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.PublicServiceResponse;
import com.digisevasolution.entity.ServiceCategory;
import com.digisevasolution.service.ServiceItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@Tag(name = "Public Service Catalog", description = "Public endpoints for browsing active Jan Seva Kendra services with bilingual fallback")
public class PublicServiceItemController {

    private final ServiceItemService serviceItemService;

    public PublicServiceItemController(ServiceItemService serviceItemService) {
        this.serviceItemService = serviceItemService;
    }

    @GetMapping
    @Operation(summary = "Get Public Active Services", description = "Fetch all active service items with resolved single-language text (en/hi with English fallback). Optionally filter by category or featured status.")
    public ResponseEntity<ApiResponse<List<PublicServiceResponse>>> getAllServicesPublic(
            @RequestParam(required = false, defaultValue = "en") String lang,
            @RequestParam(required = false) ServiceCategory category,
            @RequestParam(required = false) Boolean featured) {

        List<PublicServiceResponse> services = serviceItemService.getAllServicesPublic(lang, category, featured);
        ApiResponse<List<PublicServiceResponse>> response = ApiResponse.success("Services fetched successfully", services);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Single Public Service Detail", description = "Fetch single active service details with resolved language text.")
    public ResponseEntity<ApiResponse<PublicServiceResponse>> getServiceByIdPublic(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        PublicServiceResponse service = serviceItemService.getServiceByIdPublic(id, lang);
        ApiResponse<PublicServiceResponse> response = ApiResponse.success("Service detail fetched successfully", service);
        return ResponseEntity.ok(response);
    }
}
