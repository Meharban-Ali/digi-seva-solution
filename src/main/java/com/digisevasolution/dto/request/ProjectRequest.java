package com.digisevasolution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectRequest {

    @NotBlank(message = "English title is required")
    @Size(max = 255, message = "English title cannot exceed 255 characters")
    private String titleEn;

    @Size(max = 255, message = "Hindi title cannot exceed 255 characters")
    private String titleHi;

    private String descriptionEn;

    private String descriptionHi;

    private String imageUrl;

    private String projectUrl;

    private String categoryTag;

    private Integer displayOrder;

    private Boolean isActive;

    private Boolean isFeatured;

    public ProjectRequest() {
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
}
