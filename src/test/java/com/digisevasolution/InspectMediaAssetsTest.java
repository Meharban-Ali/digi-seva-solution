package com.digisevasolution;

import com.digisevasolution.entity.MediaAsset;
import com.digisevasolution.repository.MediaAssetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class InspectMediaAssetsTest {

    @Autowired
    private MediaAssetRepository mediaAssetRepository;

    @Test
    public void inspectAssets() {
        List<MediaAsset> assets = mediaAssetRepository.findAll();
        System.out.println("=== MEDIA ASSETS IN NEON DATABASE (" + assets.size() + ") ===");
        for (MediaAsset asset : assets) {
            System.out.println("ID: " + asset.getId());
            System.out.println("  Type: " + asset.getType());
            System.out.println("  Title: " + asset.getTitle());
            System.out.println("  Cloudinary URL: " + asset.getCloudinaryUrl());
            System.out.println("  Public ID: " + asset.getCloudinaryPublicId());
            System.out.println("  File Size Bytes: " + asset.getFileSizeBytes());
            System.out.println("  Uploaded At: " + asset.getUploadedAt());
            System.out.println("  Uploaded By: " + asset.getUploadedBy());
            System.out.println("--------------------------------------------------");
        }
    }
}
