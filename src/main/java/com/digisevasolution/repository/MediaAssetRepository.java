package com.digisevasolution.repository;

import com.digisevasolution.entity.MediaAsset;
import com.digisevasolution.entity.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    Page<MediaAsset> findAllByOrderByUploadedAtDesc(Pageable pageable);
    Page<MediaAsset> findByTypeOrderByUploadedAtDesc(MediaType type, Pageable pageable);
}
