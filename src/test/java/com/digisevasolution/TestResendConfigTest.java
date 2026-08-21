package com.digisevasolution;

import com.digisevasolution.dto.request.LoginRequest;
import com.digisevasolution.service.AuthService;
import com.digisevasolution.service.ResendEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("dev")
public class TestResendConfigTest {

    @Autowired
    private Environment environment;

    @Autowired
    private ResendEmailService resendEmailService;

    @Autowired
    private AuthService authService;

    @Value("${app.resend.api-key}")
    private String resendApiKey;

    @Value("${app.resend.from-email}")
    private String fromEmail;

    @Test
    void testResendConfiguration() {
        System.out.println("\n=======================================================");
        System.out.println("STEP 2: CHECKING LOCAL ENVIRONMENT CONFIGURATION");
        System.out.println("=======================================================");
        System.out.println("Active Profiles: " + Arrays.toString(environment.getActiveProfiles()));
        System.out.println("app.resend.api-key: " + resendApiKey);
        System.out.println("app.resend.from-email: " + fromEmail);
        System.out.println("RESEND_API_KEY env var: " + System.getenv("RESEND_API_KEY"));
        System.out.println("RESEND_FROM_EMAIL env var: " + System.getenv("RESEND_FROM_EMAIL"));

        System.out.println("\n=======================================================");
        System.out.println("STEP 1 & STEP 3: TESTING RESEND EMAIL DIRECT CALL");
        System.out.println("=======================================================");
        boolean resendResult = resendEmailService.sendOtpEmail("pashamr303@gmail.com", "654321");
        System.out.println("Resend Direct Call Returned: " + resendResult);
    }
}
