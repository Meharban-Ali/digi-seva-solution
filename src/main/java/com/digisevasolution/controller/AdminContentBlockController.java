package com.digisevasolution.controller;

import com.digisevasolution.dto.request.ContentBlockRequest;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.ContentBlockResponse;
import com.digisevasolution.entity.ContentSection;
import com.digisevasolution.service.ContentBlockService;
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
@RequestMapping("/api/admin/content")
@Tag(name = "Admin Content Management", description = "Draft/Publish content block management for website banners, announcements, and offers (JWT required)")
public class AdminContentBlockController {

    private final ContentBlockService contentBlockService;

    public AdminContentBlockController(ContentBlockService contentBlockService) {
        this.contentBlockService = contentBlockService;
    }

    @PostMapping
    @Operation(summary = "Create Content Block", description = "Create a new content block (defaults to DRAFT status).")
    public ResponseEntity<ApiResponse<ContentBlockResponse>> createContentBlock(
            @Valid @RequestBody ContentBlockRequest request) {
        ContentBlockResponse created = contentBlockService.createContentBlock(request);
        ApiResponse<ContentBlockResponse> response = ApiResponse.success("Content block created successfully", created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Content Block", description = "Update an existing content block by ID.")
    public ResponseEntity<ApiResponse<ContentBlockResponse>> updateContentBlock(
            @PathVariable Long id,
            @Valid @RequestBody ContentBlockRequest request) {
        ContentBlockResponse updated = contentBlockService.updateContentBlock(id, request);
        ApiResponse<ContentBlockResponse> response = ApiResponse.success("Content block updated successfully", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Content Block", description = "Permanently delete a content block.")
    public ResponseEntity<ApiResponse<String>> deleteContentBlock(@PathVariable Long id) {
        contentBlockService.deleteContentBlock(id);
        ApiResponse<String> response = ApiResponse.success("Content block deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish Content Block", description = "Flip content block status to PUBLISHED making it visible on public website.")
    public ResponseEntity<ApiResponse<ContentBlockResponse>> publishContentBlock(@PathVariable Long id) {
        ContentBlockResponse published = contentBlockService.publishContentBlock(id);
        ApiResponse<ContentBlockResponse> response = ApiResponse.success("Content block published successfully", published);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/unpublish")
    @Operation(summary = "Unpublish Content Block", description = "Revert content block status to DRAFT, hiding it from public website.")
    public ResponseEntity<ApiResponse<ContentBlockResponse>> unpublishContentBlock(@PathVariable Long id) {
        ContentBlockResponse unpublished = contentBlockService.unpublishContentBlock(id);
        ApiResponse<ContentBlockResponse> response = ApiResponse.success("Content block unpublished (reverted to draft)", unpublished);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List All Content Blocks (Admin View)", description = "Get paginated list of draft and published content blocks with optional section filter.")
    public ResponseEntity<ApiResponse<Page<ContentBlockResponse>>> getAllContentBlocksAdmin(
            @RequestParam(required = false) ContentSection section,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ContentBlockResponse> contentPage = contentBlockService.getAllContentBlocksAdmin(section, pageable);
        ApiResponse<Page<ContentBlockResponse>> response = ApiResponse.success("Content blocks fetched successfully", contentPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Content Block Details (Admin View)", description = "Fetch full details of a content block by ID.")
    public ResponseEntity<ApiResponse<ContentBlockResponse>> getContentBlockByIdAdmin(@PathVariable Long id) {
        ContentBlockResponse contentBlock = contentBlockService.getContentBlockByIdAdmin(id);
        ApiResponse<ContentBlockResponse> response = ApiResponse.success("Content block details fetched successfully", contentBlock);
        return ResponseEntity.ok(response);
    }
}
