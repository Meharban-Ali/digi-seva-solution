package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class TestCorsAndAdminEndpointsExecutionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCorsPreflightAndAdminCategories() throws Exception {
        System.out.println("\n=== TESTING CORS PREFLIGHT & ADMIN CATEGORIES ===");

        // Test OPTIONS Preflight with Origin header
        MvcResult optionsResult = mockMvc.perform(options("/api/admin/categories")
                        .header("Origin", "https://digisevasolution.online")
                        .header("Access-Control-Request-Method", "GET"))
                .andReturn();

        System.out.println("OPTIONS Status: " + optionsResult.getResponse().getStatus());
        if (optionsResult.getResolvedException() != null) {
            System.err.println("OPTIONS Exception:");
            optionsResult.getResolvedException().printStackTrace();
        }

        // Test GET /api/admin/categories with Origin header
        MvcResult getResult = mockMvc.perform(get("/api/admin/categories")
                        .header("Origin", "https://digisevasolution.online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println("GET Status: " + getResult.getResponse().getStatus());
        if (getResult.getResolvedException() != null) {
            System.err.println("GET Exception:");
            getResult.getResolvedException().printStackTrace();
        }
    }
}
