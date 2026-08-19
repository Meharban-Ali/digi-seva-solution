package com.digisevasolution;

import com.digisevasolution.entity.MediaType;
import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.repository.ServiceItemRepository;
import com.digisevasolution.service.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("dev")
public class FixFeaturedServiceImagesTest {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private byte[] createDistinctTestImage(Color bgColor, String labelText) throws IOException {
        BufferedImage image = new BufferedImage(800, 450, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, 800, 450);
        g.setColor(Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 36));
        g.drawString(labelText, 80, 225);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    @Test
    public void uploadDistinctImagesAndUpdateServices() throws Exception {
        System.out.println("=== 1. UPLOADING DISTINCT BRAND NEW IMAGES TO CLOUDINARY WITH UNIQUE PUBLIC IDs ===");

        // ID 1: Aadhaar Card Update
        byte[] bytesAadhaar = createDistinctTestImage(new Color(15, 23, 42), "Aadhaar Card Update Service");
        MockMultipartFile fileAadhaar = new MockMultipartFile("file", "aadhaar_card_banner.png", "image/png", bytesAadhaar);
        String urlAadhaar = (String) cloudinaryService.uploadFile(fileAadhaar, MediaType.IMAGE).get("secure_url");

        // ID 4: Web Development
        byte[] bytesWeb = createDistinctTestImage(new Color(30, 58, 138), "Web Development Services");
        MockMultipartFile fileWeb = new MockMultipartFile("file", "web_development_banner.png", "image/png", bytesWeb);
        String urlWeb = (String) cloudinaryService.uploadFile(fileWeb, MediaType.IMAGE).get("secure_url");

        // ID 5: App Development
        byte[] bytesApp = createDistinctTestImage(new Color(6, 78, 59), "Mobile App Development Services");
        MockMultipartFile fileApp = new MockMultipartFile("file", "mobile_app_banner.png", "image/png", bytesApp);
        String urlApp = (String) cloudinaryService.uploadFile(fileApp, MediaType.IMAGE).get("secure_url");

        // ID 6: Vehicle Registration & RC Services
        byte[] bytesVehicle = createDistinctTestImage(new Color(120, 53, 15), "Vehicle Registration & RC Services");
        MockMultipartFile fileVehicle = new MockMultipartFile("file", "vehicle_registration_rc_banner.png", "image/png", bytesVehicle);
        String urlVehicle = (String) cloudinaryService.uploadFile(fileVehicle, MediaType.IMAGE).get("secure_url");

        // ID 7: Driving License / Hindi Banner
        byte[] bytesDl = createDistinctTestImage(new Color(88, 28, 135), "Driving License & RTO Services");
        MockMultipartFile fileDl = new MockMultipartFile("file", "rto_driving_license_banner.png", "image/png", bytesDl);
        String urlDl = (String) cloudinaryService.uploadFile(fileDl, MediaType.IMAGE).get("secure_url");

        System.out.println("\n=== 2. UPDATING NEON DATABASE SERVICE_ITEMS WITH DISTINCT UNIQUE IMAGE URLs ===");

        jdbcTemplate.update("UPDATE service_items SET image_url = ? WHERE id = 1", urlAadhaar);
        jdbcTemplate.update("UPDATE service_items SET image_url = ? WHERE id = 4", urlWeb);
        jdbcTemplate.update("UPDATE service_items SET image_url = ? WHERE id = 5", urlApp);
        jdbcTemplate.update("UPDATE service_items SET image_url = ? WHERE id = 6", urlVehicle);
        jdbcTemplate.update("UPDATE service_items SET image_url = ? WHERE id = 7", urlDl);

        // Also ensure IDs 1, 4, 5, 6 are featured so the 4 featured cards show 4 distinct images!
        jdbcTemplate.update("UPDATE service_items SET is_featured = true WHERE id IN (1, 4, 5, 6)");
        jdbcTemplate.update("UPDATE service_items SET is_featured = false WHERE id NOT IN (1, 4, 5, 6)");

        System.out.println("\n=== 3. RAW SQL QUERY OUTPUT: SELECT id, name_en, image_url, is_featured FROM service_items ORDER BY id; ===");
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, name_en, image_url, is_featured FROM service_items ORDER BY id");
        for (Map<String, Object> row : rows) {
            System.out.println("ID: " + row.get("id") + " | Name EN: [" + row.get("name_en") + "] | Featured: [" + row.get("is_featured") + "] | Image URL: [" + row.get("image_url") + "]");
        }
    }
}
