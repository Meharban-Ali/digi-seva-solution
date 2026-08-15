package com.digisevasolution.controller;

import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.MediaAssetResponse;
import com.digisevasolution.entity.MediaType;
import com.digisevasolution.security.CustomUserDetails;
import com.digisevasolution.service.MediaAssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/media")
@Tag(name = "Admin Media Upload", description = "Multipart file upload handling for Cloudinary media assets (IMAGE, AUDIO, VIDEO) (JWT required)")
public class AdminMediaAssetController {

    private final MediaAssetService mediaAssetService;

    public AdminMediaAssetController(MediaAssetService mediaAssetService) {
        this.mediaAssetService = mediaAssetService;
    }

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Media File", description = "Upload file to Cloudinary and store asset metadata. Validates file format and 10MB/50MB size limits.")
    public ResponseEntity<ApiResponse<MediaAssetResponse>> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") MediaType type,
            @RequestParam(value = "title", required = false) String title,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long adminId = (userDetails != null && userDetails.getAdminUser() != null)
                ? userDetails.getAdminUser().getId()
                : null;

        MediaAssetResponse uploaded = mediaAssetService.uploadMedia(file, type, title, adminId);
        ApiResponse<MediaAssetResponse> response = ApiResponse.success("Media file uploaded successfully", uploaded);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List Media Library Assets", description = "Get paginated listing of media assets ordered by upload timestamp descending.")
    public ResponseEntity<ApiResponse<Page<MediaAssetResponse>>> getAllMediaAdmin(
            @RequestParam(required = false) MediaType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MediaAssetResponse> mediaPage = mediaAssetService.getAllMediaAdmin(type, pageable);
        ApiResponse<Page<MediaAssetResponse>> response = ApiResponse.success("Media library fetched successfully", mediaPage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Media Asset", description = "Atomically destroy asset in Cloudinary and remove database metadata record.")
    public ResponseEntity<ApiResponse<String>> deleteMediaAsset(@PathVariable Long id) {
        mediaAssetService.deleteMediaAsset(id);
        ApiResponse<String> response = ApiResponse.success("Media asset deleted successfully from Cloudinary and database");
        return ResponseEntity.ok(response);
    }
}
