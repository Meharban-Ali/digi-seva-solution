package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCorsIllegalArgumentExceptionTest {

    @Test
    void testCorsConfigurationConflict() {
        System.out.println("=== TESTING CORS CONFIGURATION CONFLICT ===");
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = List.of("https://digisevasolution.online", "http://localhost:5173");

        try {
            config.setAllowedOriginPatterns(origins);
            config.setAllowedOrigins(origins);
            config.checkOrigin("https://digisevasolution.online");
            System.out.println("No exception thrown during checkOrigin");
        } catch (Exception e) {
            System.err.println("EXACT CORS EXCEPTION CAUGHT:");
            e.printStackTrace();
        }
    }
}
