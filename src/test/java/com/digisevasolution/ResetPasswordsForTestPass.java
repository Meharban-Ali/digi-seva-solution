package com.digisevasolution;

import com.digisevasolution.entity.AdminUser;
import com.digisevasolution.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class ResetPasswordsForTestPass {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    public void resetPasswords() {
        List<AdminUser> users = adminUserRepository.findAll();
        for (AdminUser u : users) {
            u.setPasswordHash("$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te");
            u.setFirstLogin(true);
            adminUserRepository.save(u);
            System.out.println("RESET: " + u.getEmail() + " to Admin@12345 (isFirstLogin=true)");
        }
    }
}
