package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BcryptTest {

    @Test
    public void verifySeededPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String seededHash = "$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te";
        boolean matches = encoder.matches("Admin@12345", seededHash);
        assertTrue(matches, "Seeded BCrypt hash must match default password 'Admin@12345'");
    }
}
