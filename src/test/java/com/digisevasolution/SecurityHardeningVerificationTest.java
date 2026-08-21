package com.digisevasolution;

import com.digisevasolution.dto.request.LoginRequest;
import com.digisevasolution.dto.request.VerifyOtpRequest;
import com.digisevasolution.entity.AdminUser;
import com.digisevasolution.entity.OtpToken;
import com.digisevasolution.exception.InvalidOtpException;
import com.digisevasolution.repository.AdminUserRepository;
import com.digisevasolution.repository.OtpTokenRepository;
import com.digisevasolution.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SecurityHardeningVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private com.digisevasolution.security.RateLimitFilter rateLimitFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        if (rateLimitFilter != null) {
            rateLimitFilter.clearTrackers();
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void testOtpFailedAttemptsLockout() {
        System.out.println("\n=======================================================");
        System.out.println("VERIFYING OTP BRUTE-FORCE LOCKOUT (MAX 5 ATTEMPTS)");
        System.out.println("=======================================================");

        String email = "lockout_test@digisevasolution.com";
        adminUserRepository.findByEmail(email).orElseGet(() -> {
            return adminUserRepository.save(new AdminUser(email, passwordEncoder.encode("password123"), "Lockout User"));
        });

        OtpToken otpToken = new OtpToken(email, "123456", Instant.now().plusSeconds(600));
        otpTokenRepository.save(otpToken);

        VerifyOtpRequest wrongRequest = new VerifyOtpRequest(email, "999999");

        // Attempts 1 to 4 should fail with attempts remaining message
        for (int i = 1; i <= 4; i++) {
            final int attemptNum = i;
            InvalidOtpException ex = assertThrows(InvalidOtpException.class, () -> {
                authService.verifyOtpAndLogin(wrongRequest);
            });
            assertTrue(ex.getMessage().contains("attempts remaining"), "Attempt " + attemptNum + " should show remaining attempts");
        }

        // 5th attempt should lock out the token
        InvalidOtpException lockoutEx = assertThrows(InvalidOtpException.class, () -> {
            authService.verifyOtpAndLogin(wrongRequest);
        });
        assertTrue(lockoutEx.getMessage().contains("Maximum OTP verification attempts exceeded"), "5th attempt must lock out token");

        // Verify in DB that token is marked as verified (invalidated)
        OtpToken updatedToken = otpTokenRepository.findById(otpToken.getId()).orElseThrow();
        assertTrue(updatedToken.isVerified(), "OTP token must be marked as verified/invalidated after lockout");
        assertEquals(5, updatedToken.getFailedAttempts(), "Failed attempts count must be 5");
    }

    @Test
    void testSecurityHeadersPresent() throws Exception {
        System.out.println("\n=======================================================");
        System.out.println("VERIFYING HTTP SECURITY HEADERS");
        System.out.println("=======================================================");

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"));
    }

    @Test
    void testRateLimitingFilterOnAuthEndpoint() throws Exception {
        System.out.println("\n=======================================================");
        System.out.println("VERIFYING RATE LIMITING FILTER ON /api/admin/auth/login");
        System.out.println("=======================================================");

        try {
            String testIp = "192.168.99.100";
            LoginRequest loginPayload = new LoginRequest("test_ratelimit@example.com", "wrongpass");
            String jsonPayload = objectMapper.writeValueAsString(loginPayload);

            int lastStatus = 0;
            for (int i = 1; i <= 12; i++) {
                MvcResult result = mockMvc.perform(post("/api/admin/auth/login")
                                .header("X-Forwarded-For", testIp)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPayload))
                        .andReturn();
                lastStatus = result.getResponse().getStatus();
                if (i <= 10) {
                    assertTrue(lastStatus == 401 || lastStatus == 200, "Requests 1-10 should return normal status (401/200), got: " + lastStatus);
                } else {
                    assertEquals(429, lastStatus, "Request " + i + " (>10 limit) must return 429 TOO_MANY_REQUESTS");
                }
            }
        } finally {
            if (rateLimitFilter != null) {
                rateLimitFilter.clearTrackers();
            }
        }
    }
}
