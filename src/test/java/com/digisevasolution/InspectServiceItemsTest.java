package com.digisevasolution;

import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.repository.ServiceItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class InspectServiceItemsTest {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Test
    public void inspectServices() {
        List<ServiceItem> services = serviceItemRepository.findAll();
        System.out.println("=== SERVICE ITEMS IN NEON DATABASE (" + services.size() + ") ===");
        for (ServiceItem s : services) {
            System.out.println("ID: " + s.getId() + " | Name EN: [" + s.getNameEn() + "] | Image URL: [" + s.getImageUrl() + "]");
        }
    }
}
