package com.digisevasolution.dto.response;

import com.digisevasolution.entity.ServiceCategory;

import java.math.BigDecimal;

public class PublicServiceResponse {

    private Long id;
    private String name;
    private String description;
    private ServiceCategory category;
    private BigDecimal price;
    private String imageUrl;
    private boolean isFeatured;
    private Integer displayOrder;

    public PublicServiceResponse() {
    }

    public PublicServiceResponse(Long id, String name, String description, ServiceCategory category,
                                 BigDecimal price, String imageUrl, boolean isFeatured, Integer displayOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
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
