package com.digisevasolution;

import com.digisevasolution.entity.AdminUser;
import com.digisevasolution.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class InspectAdminUsersTest {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    public void printAdminUsers() {
        List<AdminUser> users = adminUserRepository.findAll();
        System.out.println("=== NEON DB ADMIN USERS COUNT: " + users.size() + " ===");
        for (AdminUser u : users) {
            System.out.println("ID: " + u.getId() + " | Email: [" + u.getEmail() + "] | Hash: " + u.getPasswordHash() + " | isFirstLogin: " + u.isFirstLogin());
        }
    }
}
