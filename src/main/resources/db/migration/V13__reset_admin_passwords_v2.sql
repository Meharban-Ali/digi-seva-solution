-- Flyway migration V13: Reset admin passwords to Admin@12345 and force is_first_login = true
UPDATE admin_users
SET password_hash = '$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te',
    is_first_login = true,
    updated_at = CURRENT_TIMESTAMP
WHERE email IN ('pashamr303@gmail.com', 'sahanealam07860@gmail.com');
