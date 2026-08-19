package com.digisevasolution;

import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.repository.ServiceItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("dev")
public class InspectServiceItemsTest {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Test
    public void inspectServices() {
        System.out.println("=== RAW SQL QUERY: SELECT id, name_en, image_url, is_featured FROM service_items ORDER BY id; ===");
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, name_en, image_url, is_featured FROM service_items ORDER BY id");
        for (Map<String, Object> row : rows) {
            System.out.println("ID: " + row.get("id") + " | Name EN: [" + row.get("name_en") + "] | Featured: [" + row.get("is_featured") + "] | Image URL: [" + row.get("image_url") + "]");
        }
    }
}
