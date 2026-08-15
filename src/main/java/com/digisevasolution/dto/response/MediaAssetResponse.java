package com.digisevasolution.dto.response;

import com.digisevasolution.entity.MediaType;

import java.time.LocalDateTime;

public class MediaAssetResponse {

    private Long id;
    private MediaType type;
    private String cloudinaryUrl;
    private String cloudinaryPublicId;
    private String title;
    private Long fileSizeBytes;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;

    public MediaAssetResponse() {
    }

    public MediaAssetResponse(Long id, MediaType type, String cloudinaryUrl, String cloudinaryPublicId,
                              String title, Long fileSizeBytes, Long uploadedBy, LocalDateTime uploadedAt) {
        this.id = id;
        this.type = type;
        this.cloudinaryUrl = cloudinaryUrl;
        this.cloudinaryPublicId = cloudinaryPublicId;
        this.title = title;
        this.fileSizeBytes = fileSizeBytes;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getCloudinaryUrl() {
        return cloudinaryUrl;
    }

    public void setCloudinaryUrl(String cloudinaryUrl) {
        this.cloudinaryUrl = cloudinaryUrl;
    }

    public String getCloudinaryPublicId() {
        return cloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        this.cloudinaryPublicId = cloudinaryPublicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
