package com.digisevasolution.dto.request;

import com.digisevasolution.entity.ContentSection;
import com.digisevasolution.entity.ContentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContentBlockRequest {

    @NotNull(message = "Section is required (HOME_BANNER, ABOUT_US, ANNOUNCEMENT, OFFER)")
    private ContentSection section;

    @NotBlank(message = "English title is required")
    private String titleEn;

    private String titleHi;

    @NotBlank(message = "English body content is required")
    private String bodyEn;

    private String bodyHi;

    private Long linkedMediaId;

    private ContentStatus status = ContentStatus.DRAFT;

    @Min(value = 0, message = "Display order must be zero or a positive value")
    private Integer displayOrder = 0;

    public ContentBlockRequest() {
    }

    public ContentBlockRequest(ContentSection section, String titleEn, String titleHi, String bodyEn, String bodyHi,
                               Long linkedMediaId, ContentStatus status, Integer displayOrder) {
        this.section = section;
        this.titleEn = titleEn;
        this.titleHi = titleHi;
        this.bodyEn = bodyEn;
        this.bodyHi = bodyHi;
        this.linkedMediaId = linkedMediaId;
        this.status = status != null ? status : ContentStatus.DRAFT;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public ContentSection getSection() {
        return section;
    }

    public void setSection(ContentSection section) {
        this.section = section;
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

    public String getBodyEn() {
        return bodyEn;
    }

    public void setBodyEn(String bodyEn) {
        this.bodyEn = bodyEn;
    }

    public String getBodyHi() {
        return bodyHi;
    }

    public void setBodyHi(String bodyHi) {
        this.bodyHi = bodyHi;
    }

    public Long getLinkedMediaId() {
        return linkedMediaId;
    }

    public void setLinkedMediaId(Long linkedMediaId) {
        this.linkedMediaId = linkedMediaId;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setStatus(ContentStatus status) {
        this.status = status;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
