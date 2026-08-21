package com.digisevasolution;

import com.digisevasolution.util.JwtTokenProvider;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class VerifyStep6CategoriesEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void testStep6VerifyCategoriesEndpoint() throws Exception {
        System.out.println("\n=======================================================");
        System.out.println("STEP 6 VERIFICATION: EXECUTING /api/admin/categories");
        System.out.println("=======================================================");

        UserDetails userDetails = userDetailsService.loadUserByUsername("sahanealam07860@gmail.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = tokenProvider.generateToken(auth);

        MvcResult result = mockMvc.perform(get("/api/admin/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int statusCode = result.getResponse().getStatus();
        String jsonBody = result.getResponse().getContentAsString();

        System.out.println("HTTP RESPONSE STATUS: " + statusCode);
        System.out.println("HTTP RESPONSE BODY:\n" + jsonBody);

        assertEquals(200, statusCode, "Expected HTTP 200 OK from /api/admin/categories");
        assertNotNull(jsonBody, "JSON response body must not be null");
    }
}
