package com.digisevasolution;

import com.digisevasolution.dto.request.LoginRequest;
import com.digisevasolution.dto.request.VerifyOtpRequest;
import com.digisevasolution.dto.response.JwtAuthResponse;
import com.digisevasolution.entity.OtpToken;
import com.digisevasolution.repository.OtpTokenRepository;
import com.digisevasolution.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
public class VerifyStep1To6OtpLoginTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private com.digisevasolution.repository.AdminUserRepository adminUserRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void testStep1To6FullOtpFlow() {
        System.out.println("\n=======================================================");
        System.out.println("STEP 1: INITIATING LOCAL ADMIN LOGIN & OTP GENERATION");
        System.out.println("=======================================================");

        String email = "sahanealam07860@gmail.com";

        // Ensure user exists and has password 'admin123'
        com.digisevasolution.entity.AdminUser user = adminUserRepository.findByEmail(email).orElseGet(() -> {
            return new com.digisevasolution.entity.AdminUser(email, passwordEncoder.encode("admin123"), "Admin User");
        });
        user.setPasswordHash(passwordEncoder.encode("admin123"));
        adminUserRepository.save(user);

        LoginRequest loginRequest = new LoginRequest(email, "admin123");

        // Execute initiateLogin
        authService.initiateLogin(loginRequest);

        System.out.println("\n=======================================================");
        System.out.println("STEP 4: VERIFYING DATABASE RECORD FOR GENERATED OTP");
        System.out.println("=======================================================");

        Optional<OtpToken> otpTokenOpt = otpTokenRepository.findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(email);
        assertTrue(otpTokenOpt.isPresent(), "OTP token must be generated and stored in PostgreSQL database");

        OtpToken otpToken = otpTokenOpt.get();
        System.out.println("Stored OTP Token Email: " + otpToken.getEmail());
        System.out.println("Stored OTP Code: " + otpToken.getOtpCode());
        System.out.println("Stored OTP Expires At: " + otpToken.getExpiresAt());

        System.out.println("\n=======================================================");
        System.out.println("STEP 6: VERIFYING OTP VERIFICATION & JWT TOKEN ISSUANCE");
        System.out.println("=======================================================");

        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest(email, otpToken.getOtpCode());
        JwtAuthResponse jwtResponse = authService.verifyOtpAndLogin(verifyOtpRequest);

        assertNotNull(jwtResponse, "JWT auth response must not be null");
        assertNotNull(jwtResponse.getAccessToken(), "JWT access token must be generated");
        System.out.println("Verification Success! Issued JWT Token: " + jwtResponse.getAccessToken().substring(0, 20) + "...");
    }
}
