package com.digisevasolution;

import com.digisevasolution.dto.response.CategoryResponse;
import com.digisevasolution.dto.response.ServiceItemResponse;
import com.digisevasolution.entity.Category;
import com.digisevasolution.entity.DeliveryMode;
import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.repository.CategoryRepository;
import com.digisevasolution.repository.ServiceItemRepository;
import com.digisevasolution.service.CategoryService;
import com.digisevasolution.service.ServiceItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class TestEntityRelationEdgeCasesTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void testSerializationWithNullAndAttachedCategory() throws Exception {
        System.out.println("=== TESTING SERIALIZATION EDGE CASES ===");

        // 1. Create a Category
        Category category = categoryRepository.save(new Category("Cat Edge", "कैट एज", "cat-edge", "Cpu", 99, true));

        // 2. Create Service with category assigned
        ServiceItem itemWithCat = new ServiceItem(
                "Svc With Cat", "सेवा वर्ग सहित", "Desc", "विवरण",
                DeliveryMode.ONLINE, BigDecimal.valueOf(500), null, true, false, 1
        );
        itemWithCat.setCategory(category);
        serviceItemRepository.save(itemWithCat);

        // 3. Create Service without category (null)
        ServiceItem itemWithoutCat = new ServiceItem(
                "Svc Without Cat", "सेवा वर्ग रहित", "Desc", "विवरण",
                DeliveryMode.VISIT_REQUIRED, BigDecimal.valueOf(200), null, true, false, 2
        );
        serviceItemRepository.save(itemWithoutCat);

        // 4. Test getAllCategoriesAdmin & JSON Serialization
        List<CategoryResponse> adminCategories = categoryService.getAllCategoriesAdmin();
        String jsonCats = objectMapper.writeValueAsString(adminCategories);
        assertNotNull(jsonCats);
        System.out.println("Admin Categories JSON length: " + jsonCats.length());

        // 5. Test getAllServicesAdmin & JSON Serialization
        Page<ServiceItemResponse> adminServices = serviceItemService.getAllServicesAdmin(PageRequest.of(0, 50));
        String jsonSvcs = objectMapper.writeValueAsString(adminServices);
        assertNotNull(jsonSvcs);
        System.out.println("Admin Services JSON length: " + jsonSvcs.length());

        System.out.println("=== ALL SERIALIZATIONS SUCCESSFUL ===");
    }
}
