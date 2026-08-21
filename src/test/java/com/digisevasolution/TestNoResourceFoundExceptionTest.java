package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class TestNoResourceFoundExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDoubleSlashAndUnmappedPath() throws Exception {
        System.out.println("\n=== TESTING DOUBLE SLASH / UNMAPPED RESOURCE RESOLUTION ===");

        // Test POST to unmapped path /api/admin/categories_invalid
        MvcResult unmappedResult = mockMvc.perform(post("/api/admin/categories_invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nameEn\":\"Test\",\"nameHi\":\"टेस्ट\"}"))
                .andReturn();

        System.out.println("Unmapped Path Status: " + unmappedResult.getResponse().getStatus());
        System.out.println("Unmapped Path Content: " + unmappedResult.getResponse().getContentAsString());
        if (unmappedResult.getResolvedException() != null) {
            System.out.println("Unmapped Exception: " + unmappedResult.getResolvedException().getClass().getName() + ": " + unmappedResult.getResolvedException().getMessage());
        }
    }
}
