package com.digisevasolution.controller;

import com.digisevasolution.dto.request.ServiceItemRequest;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.ServiceItemResponse;
import com.digisevasolution.service.ServiceItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/services")
@Tag(name = "Admin Service Management", description = "CRUD operations for Jan Seva Kendra service items (JWT required)")
public class AdminServiceItemController {

    private final ServiceItemService serviceItemService;

    public AdminServiceItemController(ServiceItemService serviceItemService) {
        this.serviceItemService = serviceItemService;
    }

    @PostMapping
    @Operation(summary = "Create Service", description = "Add a new service item to the catalog (bilingual support).")
    public ResponseEntity<ApiResponse<ServiceItemResponse>> createService(
            @Valid @RequestBody ServiceItemRequest request) {
        ServiceItemResponse created = serviceItemService.createService(request);
        ApiResponse<ServiceItemResponse> response = ApiResponse.success("Service created successfully", created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Service", description = "Update an existing service item by ID.")
    public ResponseEntity<ApiResponse<ServiceItemResponse>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceItemRequest request) {
        ServiceItemResponse updated = serviceItemService.updateService(id, request);
        ApiResponse<ServiceItemResponse> response = ApiResponse.success("Service updated successfully", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft Delete Service", description = "Deactivate a service item (sets isActive = false).")
    public ResponseEntity<ApiResponse<String>> softDeleteService(@PathVariable Long id) {
        serviceItemService.softDeleteService(id);
        ApiResponse<String> response = ApiResponse.success("Service deactivated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List All Services (Admin View)", description = "Get paginated list of all active and inactive service items with full bilingual fields.")
    public ResponseEntity<ApiResponse<Page<ServiceItemResponse>>> getAllServicesAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ServiceItemResponse> servicesPage = serviceItemService.getAllServicesAdmin(pageable);
        ApiResponse<Page<ServiceItemResponse>> response = ApiResponse.success("Services fetched successfully", servicesPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Service Details (Admin View)", description = "Fetch full details of a specific service item by ID.")
    public ResponseEntity<ApiResponse<ServiceItemResponse>> getServiceByIdAdmin(@PathVariable Long id) {
        ServiceItemResponse service = serviceItemService.getServiceByIdAdmin(id);
        ApiResponse<ServiceItemResponse> response = ApiResponse.success("Service details fetched successfully", service);
        return ResponseEntity.ok(response);
    }
}
