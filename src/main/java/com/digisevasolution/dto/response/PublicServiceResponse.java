package com.digisevasolution.dto.response;

import com.digisevasolution.entity.DeliveryMode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PublicServiceResponse {

    private Long id;
    private String name;
    private String description;
    private DeliveryMode deliveryMode;
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private String categoryIcon;
    private BigDecimal price;
    private String imageUrl;

    @JsonProperty("isFeatured")
    private boolean isFeatured;

    private Integer displayOrder;

    public PublicServiceResponse() {
    }

    public PublicServiceResponse(Long id, String name, String description, DeliveryMode deliveryMode,
                                 Long categoryId, String categoryName, String categorySlug, String categoryIcon,
                                 BigDecimal price, String imageUrl, boolean isFeatured, Integer displayOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deliveryMode = deliveryMode;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.categoryIcon = categoryIcon;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isFeatured = isFeatured;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public boolean isFeatured() {
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
}
