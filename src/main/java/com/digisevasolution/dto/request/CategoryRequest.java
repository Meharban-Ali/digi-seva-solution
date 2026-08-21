package com.digisevasolution.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "English category name is required")
    private String nameEn;

    @NotBlank(message = "Hindi category name is required")
    private String nameHi;

    private String slug;

    private String icon = "Folder";

    @Min(value = 0, message = "Display order must be zero or positive")
    private Integer displayOrder = 0;

    private Boolean isActive = true;

    public CategoryRequest() {
    }

    public CategoryRequest(String nameEn, String nameHi, String slug, String icon, Integer displayOrder, Boolean isActive) {
        this.nameEn = nameEn;
        this.nameHi = nameHi;
        this.slug = slug;
        this.icon = icon;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.isActive = isActive != null ? isActive : true;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameHi() {
        return nameHi;
    }

    public void setNameHi(String nameHi) {
        this.nameHi = nameHi;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
}
