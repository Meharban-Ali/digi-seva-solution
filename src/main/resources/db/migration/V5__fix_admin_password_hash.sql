-- Fix admin password hashes for seeded accounts to valid BCrypt hash for 'Admin@12345'
UPDATE admin_users
SET password_hash = '$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te',
    updated_at = CURRENT_TIMESTAMP
WHERE email IN ('admin1@digisevasolution.com', 'admin2@digisevasolution.com');
