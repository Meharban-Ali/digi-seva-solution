package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class TestMockMvcAdminEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicCategoriesEndpoint() throws Exception {
        System.out.println("=== MOCKMVC TEST /api/categories ===");
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminCategoriesEndpointWithoutAuth() throws Exception {
        System.out.println("=== MOCKMVC TEST /api/admin/categories WITHOUT AUTH ===");
        // Should return 401 Unauthorized if JWT protection is working properly
        mockMvc.perform(get("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAdminServicesEndpointWithoutAuth() throws Exception {
        System.out.println("=== MOCKMVC TEST /api/admin/services WITHOUT AUTH ===");
        // Should return 401 Unauthorized if JWT protection is working properly
        mockMvc.perform(get("/api/admin/services")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
