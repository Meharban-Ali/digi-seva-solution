package com.digisevasolution.dto.response;

import com.digisevasolution.entity.ProjectItem;
import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String titleEn;
    private String titleHi;
    private String title;
    private String descriptionEn;
    private String descriptionHi;
    private String description;
    private String imageUrl;
    private String projectUrl;
    private String categoryTag;
    private Integer displayOrder;
    private Boolean isActive;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectResponse() {
    }

    public static ProjectResponse fromEntity(ProjectItem entity) {
        ProjectResponse response = new ProjectResponse();
        response.setId(entity.getId());
        response.setTitleEn(entity.getTitleEn());
        response.setTitleHi(entity.getTitleHi());
        response.setTitle(entity.getTitleEn()); // Default fallback, resolve via constructor if language specified
        response.setDescriptionEn(entity.getDescriptionEn());
        response.setDescriptionHi(entity.getDescriptionHi());
        response.setDescription(entity.getDescriptionEn());
        response.setImageUrl(entity.getImageUrl());
        response.setProjectUrl(entity.getProjectUrl());
        response.setCategoryTag(entity.getCategoryTag());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setIsActive(entity.getIsActive());
        response.setIsFeatured(entity.getIsFeatured());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleHi() {
        return titleHi;
    }

    public void setTitleHi(String titleHi) {
        this.titleHi = titleHi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionHi() {
        return descriptionHi;
    }

    public void setDescriptionHi(String descriptionHi) {
        this.descriptionHi = descriptionHi;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
