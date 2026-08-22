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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class CaptureRealJwtAdminCategoryExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private com.digisevasolution.repository.AdminUserRepository adminUserRepository;

    @Test
    void testAdminCategoriesWithJwtToken() throws Exception {
        System.out.println("\n=======================================================");
        System.out.println("CAPTURING REAL AUTHENTICATED CALL TO /api/admin/categories");
        System.out.println("=======================================================");

        String email = "pashamr303@gmail.com";
        adminUserRepository.findByEmail(email).orElseGet(() ->
                adminUserRepository.save(new com.digisevasolution.entity.AdminUser(email, "passhash", "Admin User"))
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = tokenProvider.generateToken(auth);

        System.out.println("Generated JWT Token for sahanealam07860@gmail.com: " + token.substring(0, 20) + "...");

        MvcResult result = mockMvc.perform(get("/api/admin/categories")
                        .header("Authorization", "Bearer " + token)
                        .header("Origin", "https://digisevasolution.online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println("HTTP Response Status: " + result.getResponse().getStatus());
        System.out.println("HTTP Response Content: " + result.getResponse().getContentAsString());

        if (result.getResolvedException() != null) {
            System.err.println("\n=== REAL EXCEPTION CAPTURED ===");
            result.getResolvedException().printStackTrace();
        } else {
            System.out.println("\nNo exception thrown! Endpoint executed cleanly.");
        }

        System.out.println("\n=======================================================");
        System.out.println("CAPTURING REAL AUTHENTICATED CALL TO /api/admin/services");
        System.out.println("=======================================================");

        MvcResult servicesResult = mockMvc.perform(get("/api/admin/services")
                        .header("Authorization", "Bearer " + token)
                        .header("Origin", "https://digisevasolution.online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println("HTTP Response Status: " + servicesResult.getResponse().getStatus());
        System.out.println("HTTP Response Content: " + servicesResult.getResponse().getContentAsString());

        if (servicesResult.getResolvedException() != null) {
            System.err.println("\n=== REAL EXCEPTION CAPTURED FOR SERVICES ===");
            servicesResult.getResolvedException().printStackTrace();
        } else {
            System.out.println("\nNo exception thrown! Services endpoint executed cleanly.");
        }
    }
}
