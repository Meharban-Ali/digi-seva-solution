package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class TestResendSandboxTest {

    @Test
    void testResendApiDirectly() {
        String apiKey = "re_BP1Cjuh6_5epDSPt613gebMHmGUzm3sAK";
        String fromEmail = "onboarding@resend.dev";
        String toEmail = "sahanealam07860@gmail.com";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "from", "Digi Seva Solution <" + fromEmail + ">",
                "to", List.of(toEmail),
                "subject", "Test OTP",
                "html", "<p>Test</p>"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange("https://api.resend.com/emails", HttpMethod.POST, entity, String.class);
            System.out.println("Resend Direct Status: " + response.getStatusCode());
            System.out.println("Resend Direct Body: " + response.getBody());
        } catch (HttpClientErrorException ex) {
            System.out.println("Resend Error Status: " + ex.getStatusCode());
            System.out.println("Resend Error Response Body: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            System.out.println("General Exception: " + ex.getMessage());
        }
    }
}
