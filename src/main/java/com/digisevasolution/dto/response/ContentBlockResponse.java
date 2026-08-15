package com.digisevasolution.dto.response;

import com.digisevasolution.entity.ContentSection;
import com.digisevasolution.entity.ContentStatus;

import java.time.LocalDateTime;

public class ContentBlockResponse {

    private Long id;
    private ContentSection section;
    private String titleEn;
    private String titleHi;
    private String bodyEn;
    private String bodyHi;
    private Long linkedMediaId;
    private ContentStatus status;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContentBlockResponse() {
    }

    public ContentBlockResponse(Long id, ContentSection section, String titleEn, String titleHi, String bodyEn, String bodyHi,
                                Long linkedMediaId, ContentStatus status, Integer displayOrder,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.section = section;
        this.titleEn = titleEn;
        this.titleHi = titleHi;
        this.bodyEn = bodyEn;
        this.bodyHi = bodyHi;
        this.linkedMediaId = linkedMediaId;
        this.status = status;
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
