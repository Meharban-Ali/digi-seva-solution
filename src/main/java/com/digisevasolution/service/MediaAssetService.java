package com.digisevasolution.service;

import com.digisevasolution.dto.response.MediaAssetResponse;
import com.digisevasolution.entity.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MediaAssetService {
    MediaAssetResponse uploadMedia(MultipartFile file, MediaType type, String title, Long uploadedByAdminId);
    Page<MediaAssetResponse> getAllMediaAdmin(MediaType type, Pageable pageable);
    void deleteMediaAsset(Long id);
}
