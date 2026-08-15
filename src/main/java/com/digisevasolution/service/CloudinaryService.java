package com.digisevasolution.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.digisevasolution.entity.MediaType;
import com.digisevasolution.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    @Value("${app.cloudinary.cloud-name:placeholder}")
    private String cloudName;

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> uploadFile(MultipartFile file, MediaType type) {
        // Fallback for local testing when Cloudinary credentials are not configured
        if (cloudName == null || cloudName.isBlank() || cloudName.contains("placeholder")) {
            logger.warn("Cloudinary API credentials not configured. Generating local fallback URL.");
            String mockPublicId = "digiseva_mock_" + UUID.randomUUID().toString().substring(0, 8);
            String mockUrl = "https://res.cloudinary.com/demo/image/upload/sample.jpg";
            return Map.of(
                    "secure_url", mockUrl,
                    "public_id", mockPublicId
            );
        }

        try {
            String resourceType = switch (type) {
                case VIDEO -> "video";
                case AUDIO -> "video"; // Cloudinary handles audio files under 'video' resource type
                default -> "image";
            };

            Map options = ObjectUtils.asMap(
                    "folder", "digi_seva_solution",
                    "resource_type", resourceType
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult;
        } catch (IOException ex) {
            logger.error("Cloudinary file upload failed: {}", ex.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file to Cloudinary: " + ex.getMessage());
        }
    }

    public void deleteFile(String publicId) {
        if (cloudName == null || cloudName.isBlank() || cloudName.contains("placeholder")) {
            logger.warn("Cloudinary credentials not configured. Skipping remote destruction for mock publicId: {}", publicId);
            return;
        }

        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            if (!"ok".equalsIgnoreCase(resultStatus) && !"not found".equalsIgnoreCase(resultStatus)) {
                logger.error("Cloudinary deletion failed for publicId [{}]: {}", publicId, result);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Cloudinary remote deletion failed for asset ID: " + publicId);
            }
            logger.info("Successfully destroyed asset [{}] in Cloudinary.", publicId);
        } catch (IOException ex) {
            logger.error("Error destroying asset [{}] in Cloudinary: {}", publicId, ex.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting asset from Cloudinary: " + ex.getMessage());
        }
    }
}
