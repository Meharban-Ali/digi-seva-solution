package com.digisevasolution.service;

import com.digisevasolution.dto.request.ContentBlockRequest;
import com.digisevasolution.dto.response.ContentBlockResponse;
import com.digisevasolution.dto.response.PublicContentResponse;
import com.digisevasolution.entity.ContentSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentBlockService {
    // Admin operations
    ContentBlockResponse createContentBlock(ContentBlockRequest request);
    ContentBlockResponse updateContentBlock(Long id, ContentBlockRequest request);
    void deleteContentBlock(Long id);
    ContentBlockResponse publishContentBlock(Long id);
    ContentBlockResponse unpublishContentBlock(Long id);
    Page<ContentBlockResponse> getAllContentBlocksAdmin(ContentSection section, Pageable pageable);
    ContentBlockResponse getContentBlockByIdAdmin(Long id);

    // Public operations
    List<PublicContentResponse> getContentBlocksPublic(ContentSection section, String lang);
    PublicContentResponse getContentBlockByIdPublic(Long id, String lang);
}
