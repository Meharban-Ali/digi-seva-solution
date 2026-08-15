-- Reset partner admin accounts to fresh temporary password 'Admin@12345'
-- Sets is_first_login = true to enforce password change on initial login

UPDATE admin_users
SET password_hash = '$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te',
    is_first_login = TRUE,
    updated_at = CURRENT_TIMESTAMP
WHERE email IN ('pashamr303@gmail.com', 'sahanealam07860@gmail.com');
