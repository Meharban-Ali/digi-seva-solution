package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.ContentBlockRequest;
import com.digisevasolution.dto.response.ContentBlockResponse;
import com.digisevasolution.dto.response.PublicContentResponse;
import com.digisevasolution.entity.ContentBlock;
import com.digisevasolution.entity.ContentSection;
import com.digisevasolution.entity.ContentStatus;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.ContentBlockRepository;
import com.digisevasolution.service.ContentBlockService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentBlockServiceImpl implements ContentBlockService {

    private final ContentBlockRepository contentBlockRepository;

    public ContentBlockServiceImpl(ContentBlockRepository contentBlockRepository) {
        this.contentBlockRepository = contentBlockRepository;
    }

    @Override
    @Transactional
    public ContentBlockResponse createContentBlock(ContentBlockRequest request) {
        ContentBlock contentBlock = new ContentBlock(
                request.getSection(),
                request.getTitleEn(),
                request.getTitleHi(),
                request.getBodyEn(),
                request.getBodyHi(),
                request.getLinkedMediaId(),
                request.getStatus() != null ? request.getStatus() : ContentStatus.DRAFT,
                request.getDisplayOrder() != null ? request.getDisplayOrder() : 0
        );

        ContentBlock saved = contentBlockRepository.save(contentBlock);
        return mapToAdminResponse(saved);
    }

    @Override
    @Transactional
    public ContentBlockResponse updateContentBlock(Long id, ContentBlockRequest request) {
        ContentBlock contentBlock = contentBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBlock", "id", id));

        contentBlock.setSection(request.getSection());
        contentBlock.setTitleEn(request.getTitleEn());
        contentBlock.setTitleHi(request.getTitleHi());
        contentBlock.setBodyEn(request.getBodyEn());
        contentBlock.setBodyHi(request.getBodyHi());
        contentBlock.setLinkedMediaId(request.getLinkedMediaId());
        if (request.getStatus() != null) {
            contentBlock.setStatus(request.getStatus());
        }
        if (request.getDisplayOrder() != null) {
            contentBlock.setDisplayOrder(request.getDisplayOrder());
        }

        ContentBlock updated = contentBlockRepository.save(contentBlock);
        return mapToAdminResponse(updated);
    }

    @Override
    @Transactional
    public void deleteContentBlock(Long id) {
        ContentBlock contentBlock = contentBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBlock", "id", id));
        contentBlockRepository.delete(contentBlock);
    }

    @Override
    @Transactional
    public ContentBlockResponse publishContentBlock(Long id) {
        ContentBlock contentBlock = contentBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBlock", "id", id));
        contentBlock.setStatus(ContentStatus.PUBLISHED);
        ContentBlock updated = contentBlockRepository.save(contentBlock);
        return mapToAdminResponse(updated);
    }

    @Override
    @Transactional
    public ContentBlockResponse unpublishContentBlock(Long id) {
        ContentBlock contentBlock = contentBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBlock", "id", id));
        contentBlock.setStatus(ContentStatus.DRAFT);
        ContentBlock updated = contentBlockRepository.save(contentBlock);
        return mapToAdminResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentBlockResponse> getAllContentBlocksAdmin(ContentSection section, Pageable pageable) {
        Page<ContentBlock> page;
        if (section != null) {
            page = contentBlockRepository.findBySection(section, pageable);
        } else {
            page = contentBlockRepository.findAll(pageable);
        }
        return page.map(this::mapToAdminResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ContentBlockResponse getContentBlockByIdAdmin(Long id) {
        ContentBlock contentBlock = contentBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBlock", "id", id));
        return mapToAdminResponse(contentBlock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicContentResponse> getContentBlocksPublic(ContentSection section, String lang) {
        List<ContentBlock> blocks;
        if (section != null) {
            blocks = contentBlockRepository.findBySectionAndStatusOrderByDisplayOrderAscCreatedAtDesc(section, ContentStatus.PUBLISHED);
        } else {
            blocks = contentBlockRepository.findByStatusOrderByDisplayOrderAscCreatedAtDesc(ContentStatus.PUBLISHED);
        }

        return blocks.stream()
                .map(block -> mapToPublicResponse(block, lang))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PublicContentResponse getContentBlockByIdPublic(Long id, String lang) {
        ContentBlock contentBlock = contentBlockRepository.findByIdAndStatus(id, ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBlock", "id", id));
        return mapToPublicResponse(contentBlock, lang);
    }

    // Helper mapping for Admin response
    private ContentBlockResponse mapToAdminResponse(ContentBlock block) {
        return new ContentBlockResponse(
                block.getId(),
                block.getSection(),
                block.getTitleEn(),
                block.getTitleHi(),
                block.getBodyEn(),
                block.getBodyHi(),
                block.getLinkedMediaId(),
                block.getStatus(),
                block.getDisplayOrder(),
                block.getCreatedAt(),
                block.getUpdatedAt()
        );
    }

    // Helper mapping for Public single-language view with Fallback logic
    private PublicContentResponse mapToPublicResponse(ContentBlock block, String lang) {
        String resolvedTitle;
        String resolvedBody;

        if ("hi".equalsIgnoreCase(lang)) {
            resolvedTitle = (block.getTitleHi() != null && !block.getTitleHi().isBlank())
                    ? block.getTitleHi()
                    : block.getTitleEn();
            resolvedBody = (block.getBodyHi() != null && !block.getBodyHi().isBlank())
                    ? block.getBodyHi()
                    : block.getBodyEn();
        } else {
            resolvedTitle = block.getTitleEn();
            resolvedBody = block.getBodyEn();
        }

        return new PublicContentResponse(
                block.getId(),
                block.getSection(),
                resolvedTitle,
                resolvedBody,
                block.getLinkedMediaId(),
                block.getDisplayOrder()
        );
    }
}
