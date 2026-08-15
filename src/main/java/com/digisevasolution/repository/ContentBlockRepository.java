package com.digisevasolution.repository;

import com.digisevasolution.entity.ContentBlock;
import com.digisevasolution.entity.ContentSection;
import com.digisevasolution.entity.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentBlockRepository extends JpaRepository<ContentBlock, Long> {
    Page<ContentBlock> findAll(Pageable pageable);
    Page<ContentBlock> findBySection(ContentSection section, Pageable pageable);

    List<ContentBlock> findByStatusOrderByDisplayOrderAscCreatedAtDesc(ContentStatus status);
    List<ContentBlock> findBySectionAndStatusOrderByDisplayOrderAscCreatedAtDesc(ContentSection section, ContentStatus status);

    Optional<ContentBlock> findByIdAndStatus(Long id, ContentStatus status);
}
