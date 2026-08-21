package com.digisevasolution.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CategoryResponse {

    private Long id;
    private String nameEn;
    private String nameHi;
    private String name; // resolved single-language name for public consumption
    private String slug;
    private String icon;
    private Integer displayOrder;

    @JsonProperty("isActive")
    private boolean isActive;

    private long serviceCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String nameEn, String nameHi, String name, String slug, String icon,
                            Integer displayOrder, boolean isActive, long serviceCount,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameHi = nameHi;
        this.name = name;
        this.slug = slug;
        this.icon = icon;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.serviceCount = serviceCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isActive() {
        return isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(long serviceCount) {
        this.serviceCount = serviceCount;
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
