package com.digisevasolution.service.impl;

import com.digisevasolution.dto.response.MediaAssetResponse;
import com.digisevasolution.entity.MediaAsset;
import com.digisevasolution.entity.MediaType;
import com.digisevasolution.exception.ApiException;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.MediaAssetRepository;
import com.digisevasolution.service.CloudinaryService;
import com.digisevasolution.service.MediaAssetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MediaAssetServiceImpl implements MediaAssetService {

    private static final long MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB
    private static final long MAX_MEDIA_SIZE_BYTES = 50 * 1024 * 1024; // 50 MB

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg");
    private static final Set<String> ALLOWED_AUDIO_EXTENSIONS = Set.of(".mp3", ".wav", ".aac", ".ogg", ".m4a");
    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of(".mp4", ".mkv", ".mov", ".avi", ".webm");

    private final MediaAssetRepository mediaAssetRepository;
    private final CloudinaryService cloudinaryService;

    public MediaAssetServiceImpl(MediaAssetRepository mediaAssetRepository, CloudinaryService cloudinaryService) {
        this.mediaAssetRepository = mediaAssetRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional
    public MediaAssetResponse uploadMedia(MultipartFile file, MediaType type, String title, Long uploadedByAdminId) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "File to upload cannot be empty.");
        }

        if (type == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Media type is required (IMAGE, AUDIO, or VIDEO).");
        }

        // Validate file extension & content type
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

        validateFileTypeAndSize(file, type, extension);

        // Upload file to Cloudinary
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, type);
        String secureUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        String displayTitle = (title != null && !title.isBlank()) ? title : originalFilename;

        MediaAsset mediaAsset = new MediaAsset(
                type,
                secureUrl,
                publicId,
                displayTitle,
                file.getSize(),
                uploadedByAdminId
        );

        MediaAsset saved = mediaAssetRepository.save(mediaAsset);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MediaAssetResponse> getAllMediaAdmin(MediaType type, Pageable pageable) {
        Page<MediaAsset> page;
        if (type != null) {
            page = mediaAssetRepository.findByTypeOrderByUploadedAtDesc(type, pageable);
        } else {
            page = mediaAssetRepository.findAllByOrderByUploadedAtDesc(pageable);
        }
        return page.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void deleteMediaAsset(Long id) {
        MediaAsset mediaAsset = mediaAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaAsset", "id", id));

        // Destroy file in Cloudinary first
        cloudinaryService.deleteFile(mediaAsset.getCloudinaryPublicId());

        // Remove record from database
        mediaAssetRepository.delete(mediaAsset);
    }

    private void validateFileTypeAndSize(MultipartFile file, MediaType type, String extension) {
        long fileSize = file.getSize();
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";

        switch (type) {
            case IMAGE -> {
                if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension) || (!contentType.isEmpty() && !contentType.startsWith("image/"))) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Invalid image file format or MIME type. Allowed extensions: " + ALLOWED_IMAGE_EXTENSIONS);
                }
                if (fileSize > MAX_IMAGE_SIZE_BYTES) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Image file size exceeds maximum limit of 10MB.");
                }
            }
            case AUDIO -> {
                if (!ALLOWED_AUDIO_EXTENSIONS.contains(extension) || (!contentType.isEmpty() && !contentType.startsWith("audio/"))) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Invalid audio file format or MIME type. Allowed extensions: " + ALLOWED_AUDIO_EXTENSIONS);
                }
                if (fileSize > MAX_MEDIA_SIZE_BYTES) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Audio file size exceeds maximum limit of 50MB.");
                }
            }
            case VIDEO -> {
                if (!ALLOWED_VIDEO_EXTENSIONS.contains(extension) || (!contentType.isEmpty() && !contentType.startsWith("video/"))) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Invalid video file format or MIME type. Allowed extensions: " + ALLOWED_VIDEO_EXTENSIONS);
                }
                if (fileSize > MAX_MEDIA_SIZE_BYTES) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Video file size exceeds maximum limit of 50MB.");
                }
            }
        }
    }

    private MediaAssetResponse mapToResponse(MediaAsset asset) {
        return new MediaAssetResponse(
                asset.getId(),
                asset.getType(),
                asset.getCloudinaryUrl(),
                asset.getCloudinaryPublicId(),
                asset.getTitle(),
                asset.getFileSizeBytes(),
                asset.getUploadedBy(),
                asset.getUploadedAt()
        );
    }
}
