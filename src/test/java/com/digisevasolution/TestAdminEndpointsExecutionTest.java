package com.digisevasolution;

import com.digisevasolution.controller.AdminCategoryController;
import com.digisevasolution.controller.AdminServiceItemController;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.CategoryResponse;
import com.digisevasolution.dto.response.ServiceItemResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class TestAdminEndpointsExecutionTest {

    @Autowired
    private AdminCategoryController adminCategoryController;

    @Autowired
    private AdminServiceItemController adminServiceItemController;

    @Test
    @Transactional
    void testGetAllCategoriesAdmin() {
        System.out.println("=== TESTING GET ALL CATEGORIES ADMIN ===");
        try {
            ResponseEntity<ApiResponse<List<CategoryResponse>>> response = adminCategoryController.getAllCategoriesAdmin();
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            List<CategoryResponse> categories = response.getBody().getData();
            System.out.println("Retrieved categories count: " + categories.size());
            for (CategoryResponse cat : categories) {
                System.out.println("Cat ID: " + cat.getId() + ", Name: " + cat.getNameEn() + ", ServiceCount: " + cat.getServiceCount());
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION IN getAllCategoriesAdmin:");
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    @Transactional
    void testGetAllServicesAdmin() {
        System.out.println("\n=== TESTING GET ALL SERVICES ADMIN ===");
        try {
            ResponseEntity<ApiResponse<Page<ServiceItemResponse>>> response = adminServiceItemController.getAllServicesAdmin(0, 10, "displayOrder", "ASC");
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            Page<ServiceItemResponse> page = response.getBody().getData();
            System.out.println("Retrieved services page total elements: " + page.getTotalElements());
            for (ServiceItemResponse svc : page.getContent()) {
                System.out.println("Svc ID: " + svc.getId() + ", Name: " + svc.getNameEn() + ", CatName: " + svc.getCategoryNameEn());
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION IN getAllServicesAdmin:");
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
