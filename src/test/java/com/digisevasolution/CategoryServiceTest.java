package com.digisevasolution;

import com.digisevasolution.dto.request.CategoryRequest;
import com.digisevasolution.dto.response.CategoryResponse;
import com.digisevasolution.entity.Category;
import com.digisevasolution.entity.DeliveryMode;
import com.digisevasolution.entity.ServiceItem;
import com.digisevasolution.exception.ApiException;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.CategoryRepository;
import com.digisevasolution.repository.ServiceItemRepository;
import com.digisevasolution.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Create a base category for tests
        testCategory = new Category("Test Service Category", "परीक्षण सेवा वर्ग", "test-service-category", "Shield", 100, true);
        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void testCreateCategory_Success() {
        CategoryRequest request = new CategoryRequest("New Test Category", "नया परीक्षण वर्ग", "new-test-category", "Cpu", 101, true);
        CategoryResponse response = categoryService.createCategory(request);

        assertNotNull(response.getId());
        assertEquals("New Test Category", response.getNameEn());
        assertEquals("नया परीक्षण वर्ग", response.getNameHi());
        assertEquals("new-test-category", response.getSlug());
        assertEquals("Cpu", response.getIcon());
        assertTrue(response.isActive());
    }

    @Test
    void testCreateCategory_DuplicateSlug_ThrowsApiException() {
        CategoryRequest request = new CategoryRequest("Duplicate Slug Category", "डुप्लिकेट स्लग", "test-service-category", "Folder", 102, true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            categoryService.createCategory(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testUpdateCategory_DuplicateSlug_ThrowsApiException() {
        Category categoryTwo = categoryRepository.save(new Category("Category Two", "वर्ग 2", "category-two", "Zap", 103, true));

        CategoryRequest updateRequest = new CategoryRequest("Category Two Updated", "वर्ग 2 अद्यतन", "test-service-category", "Zap", 103, true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            categoryService.updateCategory(categoryTwo.getId(), updateRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testDeleteCategory_BlockedWhenServicesAttached_ThrowsApiException() {
        // Attach a ServiceItem to testCategory
        ServiceItem serviceItem = new ServiceItem(
                "Attached Service", "संलग्न सेवा", "Desc EN", "Desc HI",
                DeliveryMode.ONLINE, BigDecimal.valueOf(99.00), null, true, false, 1
        );
        serviceItem.setCategory(testCategory);
        serviceItemRepository.save(serviceItem);

        ApiException exception = assertThrows(ApiException.class, () -> {
            categoryService.deleteCategory(testCategory.getId());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cannot delete category because"));
    }

    @Test
    void testDeleteCategory_SuccessWhenNoServicesAttached() {
        Category emptyCategory = categoryRepository.save(new Category("Empty Category", "खाली वर्ग", "empty-category", "Folder", 104, true));

        categoryService.deleteCategory(emptyCategory.getId());

        assertFalse(categoryRepository.existsById(emptyCategory.getId()));
    }

    @Test
    void testGetCategoryByIdAdmin_NotFound_ThrowsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryByIdAdmin(999999L);
        });
    }
}
