package com.digisevasolution.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "content_blocks")
public class ContentBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ContentSection section;

    @Column(name = "title_en", nullable = false)
    private String titleEn;

    @Column(name = "title_hi")
    private String titleHi;

    @Column(name = "body_en", nullable = false, columnDefinition = "TEXT")
    private String bodyEn;

    @Column(name = "body_hi", columnDefinition = "TEXT")
    private String bodyHi;

    @Column(name = "linked_media_id")
    private Long linkedMediaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ContentBlock() {
    }

    public ContentBlock(ContentSection section, String titleEn, String titleHi, String bodyEn, String bodyHi,
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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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
