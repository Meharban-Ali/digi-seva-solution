package com.digisevasolution;

import com.digisevasolution.util.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class VerifyAdminServicesEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testGetAdminServicesPaginated() throws Exception {
        String token = jwtTokenProvider.generateTokenForEmail("pashamr303@gmail.com");

        MvcResult result = mockMvc.perform(get("/api/admin/services")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andReturn();

        System.out.println("\n=======================================================");
        System.out.println("VERIFIED GET /api/admin/services?page=0&size=10");
        System.out.println("RESPONSE BODY:");
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("=======================================================\n");
    }
}
