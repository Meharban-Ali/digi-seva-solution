package com.digisevasolution;

import com.digisevasolution.dto.request.CategoryRequest;
import com.digisevasolution.util.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class VerifyCategoryCreationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private com.digisevasolution.repository.AdminUserRepository adminUserRepository;

    @Test
    @Transactional
    void testCreateAdminCategoryEndpoint() throws Exception {
        System.out.println("\n=======================================================");
        System.out.println("VERIFYING POST /api/admin/categories CREATION");
        System.out.println("=======================================================");

        String email = "pashamr303@gmail.com";
        adminUserRepository.findByEmail(email).orElseGet(() ->
                adminUserRepository.save(new com.digisevasolution.entity.AdminUser(email, "passhash", "Admin User"))
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = tokenProvider.generateToken(auth);

        CategoryRequest requestPayload = new CategoryRequest(
                "Test New Category",
                "टेस्ट नई श्रेणी",
                "test-new-category",
                "Sparkles",
                99,
                true
        );

        String jsonContent = objectMapper.writeValueAsString(requestPayload);

        // Test POST /api/admin/categories
        MvcResult result = mockMvc.perform(post("/api/admin/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        int statusCode = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        System.out.println("POST /api/admin/categories Status: " + statusCode);
        System.out.println("Response Body:\n" + responseBody);

        assertEquals(201, statusCode, "Expected HTTP 201 CREATED for category creation");
        assertNotNull(responseBody, "Response body must not be null");

        // Test POST /api/admin/categories/ (with trailing slash)
        CategoryRequest requestPayload2 = new CategoryRequest(
                "Test New Category 2",
                "टेस्ट नई श्रेणी 2",
                "test-new-category-2",
                "Folder",
                100,
                true
        );

        MvcResult result2 = mockMvc.perform(post("/api/admin/categories/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPayload2)))
                .andReturn();

        System.out.println("POST /api/admin/categories/ Status: " + result2.getResponse().getStatus());
        assertEquals(201, result2.getResponse().getStatus(), "Expected HTTP 201 CREATED for category creation with trailing slash");
    }
}
