package com.digisevasolution.controller;

import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.PublicContentResponse;
import com.digisevasolution.entity.ContentSection;
import com.digisevasolution.service.ContentBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
@Tag(name = "Public Content Display", description = "Public endpoints for displaying published content blocks (banners, announcements, offers) with language fallback")
public class PublicContentBlockController {

    private final ContentBlockService contentBlockService;

    public PublicContentBlockController(ContentBlockService contentBlockService) {
        this.contentBlockService = contentBlockService;
    }

    @GetMapping
    @Operation(summary = "Get Published Content Blocks", description = "Fetch strictly PUBLISHED content blocks for a section with resolved language text (DRAFT items never exposed).")
    public ResponseEntity<ApiResponse<List<PublicContentResponse>>> getContentBlocksPublic(
            @RequestParam(required = false) ContentSection section,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        List<PublicContentResponse> contentBlocks = contentBlockService.getContentBlocksPublic(section, lang);
        ApiResponse<List<PublicContentResponse>> response = ApiResponse.success("Published content fetched successfully", contentBlocks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Single Published Content Detail", description = "Fetch single published content block detail. Returns 404 if item is DRAFT or missing.")
    public ResponseEntity<ApiResponse<PublicContentResponse>> getContentBlockByIdPublic(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        PublicContentResponse contentBlock = contentBlockService.getContentBlockByIdPublic(id, lang);
        ApiResponse<PublicContentResponse> response = ApiResponse.success("Published content detail fetched successfully", contentBlock);
        return ResponseEntity.ok(response);
    }
}
