package com.digisevasolution.dto.response;

import com.digisevasolution.entity.DeliveryMode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceItemResponse {

    private Long id;
    private String nameEn;
    private String nameHi;
    private String descriptionEn;
    private String descriptionHi;
    private DeliveryMode deliveryMode;
    private Long categoryId;
    private String categoryNameEn;
    private String categoryNameHi;
    private String categorySlug;
    private String categoryIcon;
    private BigDecimal price;
    private String imageUrl;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("isFeatured")
    private boolean isFeatured;

    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceItemResponse() {
    }

    public ServiceItemResponse(Long id, String nameEn, String nameHi, String descriptionEn, String descriptionHi,
                               DeliveryMode deliveryMode, Long categoryId, String categoryNameEn, String categoryNameHi,
                               String categorySlug, String categoryIcon, BigDecimal price, String imageUrl, boolean isActive,
                               boolean isFeatured, Integer displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameHi = nameHi;
        this.descriptionEn = descriptionEn;
        this.descriptionHi = descriptionHi;
        this.deliveryMode = deliveryMode;
        this.categoryId = categoryId;
        this.categoryNameEn = categoryNameEn;
        this.categoryNameHi = categoryNameHi;
        this.categorySlug = categorySlug;
        this.categoryIcon = categoryIcon;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.isFeatured = isFeatured;
        this.displayOrder = displayOrder;
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

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryNameEn() {
        return categoryNameEn;
    }

    public void setCategoryNameEn(String categoryNameEn) {
        this.categoryNameEn = categoryNameEn;
    }

    public String getCategoryNameHi() {
        return categoryNameHi;
    }

    public void setCategoryNameHi(String categoryNameHi) {
        this.categoryNameHi = categoryNameHi;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public boolean isFeatured() {
        return isFeatured;
    }

    public boolean getIsFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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
