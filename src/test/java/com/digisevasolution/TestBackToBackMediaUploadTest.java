package com.digisevasolution;

import com.digisevasolution.dto.response.MediaAssetResponse;
import com.digisevasolution.entity.MediaType;
import com.digisevasolution.repository.MediaAssetRepository;
import com.digisevasolution.service.MediaAssetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class TestBackToBackMediaUploadTest {

    @Autowired
    private MediaAssetService mediaAssetService;

    @Autowired
    private MediaAssetRepository mediaAssetRepository;

    @Test
    public void testBackToBackUploadsOfDifferentImages() {
        System.out.println("=== TESTING BACK-TO-BACK UPLOADS OF 2 GENUINELY DIFFERENT IMAGES ===");

        // Valid 1x1 Red PNG byte array
        byte[] imageAContent = new byte[] {
                (byte)0x89, (byte)0x50, (byte)0x4e, (byte)0x47, (byte)0x0d, (byte)0x0a, (byte)0x1a, (byte)0x0a,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0d, (byte)0x49, (byte)0x48, (byte)0x44, (byte)0x52,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
                (byte)0x08, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x90, (byte)0x77, (byte)0x53,
                (byte)0xde, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0c, (byte)0x49, (byte)0x44, (byte)0x41,
                (byte)0x54, (byte)0x08, (byte)0xd7, (byte)0x63, (byte)0xf8, (byte)0xcf, (byte)0xc0, (byte)0x00,
                (byte)0x00, (byte)0x03, (byte)0x01, (byte)0x01, (byte)0x00, (byte)0x18, (byte)0xdd, (byte)0x8d,
                (byte)0xb0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x49, (byte)0x45, (byte)0x4e,
                (byte)0x44, (byte)0xae, (byte)0x42, (byte)0x60, (byte)0x82
        };
        MockMultipartFile fileA = new MockMultipartFile(
                "file",
                "test_service_banner_red.png",
                "image/png",
                imageAContent
        );

        // Upload 1
        MediaAssetResponse responseA = mediaAssetService.uploadMedia(
                fileA,
                MediaType.IMAGE,
                "Test Service Banner Red",
                1L
        );

        System.out.println("Upload A Successful!");
        System.out.println("  ID: " + responseA.getId());
        System.out.println("  Title: " + responseA.getTitle());
        System.out.println("  URL: " + responseA.getCloudinaryUrl());
        System.out.println("  Public ID: " + responseA.getCloudinaryPublicId());
        System.out.println("  Size: " + responseA.getFileSizeBytes());

        // Valid 1x1 Blue PNG byte array (genuinely different image content)
        byte[] imageBContent = new byte[] {
                (byte)0x89, (byte)0x50, (byte)0x4e, (byte)0x47, (byte)0x0d, (byte)0x0a, (byte)0x1a, (byte)0x0a,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0d, (byte)0x49, (byte)0x48, (byte)0x44, (byte)0x52,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
                (byte)0x08, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x90, (byte)0x77, (byte)0x53,
                (byte)0xde, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0c, (byte)0x49, (byte)0x44, (byte)0x41,
                (byte)0x54, (byte)0x08, (byte)0xd7, (byte)0x63, (byte)0x60, (byte)0x60, (byte)0xf8, (byte)0x0f,
                (byte)0x00, (byte)0x01, (byte)0x04, (byte)0x01, (byte)0x01, (byte)0x11, (byte)0x24, (byte)0xbc,
                (byte)0xd4, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x49, (byte)0x45, (byte)0x4e,
                (byte)0x44, (byte)0xae, (byte)0x42, (byte)0x60, (byte)0x82
        };
        MockMultipartFile fileB = new MockMultipartFile(
                "file",
                "test_service_banner_blue.png",
                "image/png",
                imageBContent
        );

        // Upload 2 (Back-to-Back)
        MediaAssetResponse responseB = mediaAssetService.uploadMedia(
                fileB,
                MediaType.IMAGE,
                "Test Service Banner Blue",
                1L
        );

        System.out.println("\nUpload B Successful!");
        System.out.println("  ID: " + responseB.getId());
        System.out.println("  Title: " + responseB.getTitle());
        System.out.println("  URL: " + responseB.getCloudinaryUrl());
        System.out.println("  Public ID: " + responseB.getCloudinaryPublicId());
        System.out.println("  Size: " + responseB.getFileSizeBytes());

        // Assertions
        Assertions.assertNotEquals(responseA.getId(), responseB.getId(), "Database IDs must be distinct");
        Assertions.assertNotEquals(responseA.getCloudinaryUrl(), responseB.getCloudinaryUrl(), "Cloudinary URLs must be distinct");

        System.out.println("\n=== VERIFICATION PASSED: BOTH UPLOADS GOT DISTINCT CLOUDINARY URLS AND SEPARATE DB ROWS ===");
    }
}
