package com.digisevasolution.dto.request;

import com.digisevasolution.entity.ServiceCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ServiceItemRequest {

    @NotBlank(message = "English service name is required")
    private String nameEn;

    private String nameHi;

    private String descriptionEn;

    private String descriptionHi;

    @NotNull(message = "Service category is required (VISIT_REQUIRED or ONLINE)")
    private ServiceCategory category;

    @PositiveOrZero(message = "Price must be zero or a positive value")
    private BigDecimal price;

    private String imageUrl;

    private Boolean isActive = true;

    @Min(value = 0, message = "Display order must be zero or positive")
    private Integer displayOrder = 0;

    public ServiceItemRequest() {
    }

    public ServiceItemRequest(String nameEn, String nameHi, String descriptionEn, String descriptionHi,
                              ServiceCategory category, BigDecimal price, String imageUrl,
                              Boolean isActive, Integer displayOrder) {
        this.nameEn = nameEn;
        this.nameHi = nameHi;
        this.descriptionEn = descriptionEn;
        this.descriptionHi = descriptionHi;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive != null ? isActive : true;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
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

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
