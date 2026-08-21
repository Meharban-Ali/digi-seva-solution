package com.digisevasolution;

import com.digisevasolution.util.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class TestStaleJwtTokenFilterExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    void testStaleUserTokenTriggers500() throws Exception {
        System.out.println("\n=======================================================");
        System.out.println("TESTING STALE/DELETED USER JWT TOKEN BEHAVIOR");
        System.out.println("=======================================================");

        // Generate token for a user email that does NOT exist in the database
        String staleToken = tokenProvider.generateTokenForEmail("deleted_user_999@gmail.com");

        MvcResult result = mockMvc.perform(get("/api/admin/categories")
                        .header("Authorization", "Bearer " + staleToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println("HTTP Response Status: " + result.getResponse().getStatus());
        System.out.println("HTTP Response Content: " + result.getResponse().getContentAsString());

        if (result.getResolvedException() != null) {
            System.err.println("\nEXACT EXCEPTION CAPTURED:");
            result.getResolvedException().printStackTrace();
        }
    }
}
