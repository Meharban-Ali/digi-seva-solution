package com.digisevasolution.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ResendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(ResendEmailService.class);
    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    @Value("${app.resend.api-key}")
    private String resendApiKey;

    @Value("${app.resend.from-email}")
    private String fromEmail;

    private final RestTemplate restTemplate;

    public ResendEmailService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean sendOtpEmail(String toEmail, String otpCode) {
        if (resendApiKey == null || resendApiKey.isBlank() || resendApiKey.contains("placeholder")) {
            logger.warn("Resend API key is not configured. OTP [{}] for [{}] logged to console.", otpCode, toEmail);
            return true;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            String htmlBody = String.format("""
                <div style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
                    <h2>Digi Seva Solution - Admin Login OTP</h2>
                    <p>Hello,</p>
                    <p>Your One-Time Password (OTP) for admin login is:</p>
                    <div style="background: #f4f4f4; padding: 15px; border-radius: 8px; font-size: 24px; font-weight: bold; letter-spacing: 4px; display: inline-block; margin: 10px 0;">
                        %s
                    </div>
                    <p>This OTP is valid for <strong>10 minutes</strong>. Do not share this code with anyone.</p>
                    <br/>
                    <p>Regards,<br/><strong>Digi Seva Solution Security Team</strong></p>
                </div>
                """, otpCode);

            String senderAddress = (fromEmail != null && !fromEmail.isBlank() && !fromEmail.contains("placeholder") && !fromEmail.toLowerCase().endsWith("@gmail.com"))
                    ? fromEmail
                    : "onboarding@resend.dev";

            Map<String, Object> requestBody = Map.of(
                    "from", "Digi Seva Solution <" + senderAddress + ">",
                    "to", List.of(toEmail),
                    "subject", "Your Admin Login OTP - Digi Seva Solution",
                    "html", htmlBody
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(RESEND_API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("OTP email successfully sent to [{}] via Resend API.", toEmail);
                return true;
            } else {
                logger.error("Failed to send OTP email via Resend API. Response: {}", response.getBody());
                return false;
            }
        } catch (Exception ex) {
            logger.error("Error sending OTP email via Resend to [{}]: {}", toEmail, ex.getMessage());
            return true;
        }
    }
}
