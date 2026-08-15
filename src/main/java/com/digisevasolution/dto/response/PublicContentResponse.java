package com.digisevasolution.dto.response;

import com.digisevasolution.entity.ContentSection;

public class PublicContentResponse {

    private Long id;
    private ContentSection section;
    private String title;
    private String body;
    private Long linkedMediaId;
    private Integer displayOrder;

    public PublicContentResponse() {
    }

    public PublicContentResponse(Long id, ContentSection section, String title, String body, Long linkedMediaId, Integer displayOrder) {
        this.id = id;
        this.section = section;
        this.title = title;
        this.body = body;
        this.linkedMediaId = linkedMediaId;
        this.displayOrder = displayOrder;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getLinkedMediaId() {
        return linkedMediaId;
    }

    public void setLinkedMediaId(Long linkedMediaId) {
        this.linkedMediaId = linkedMediaId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
