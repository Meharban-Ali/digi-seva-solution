-- Seeds initial partner admin accounts for Digi Seva Solution
-- Temporary default password for both accounts: Admin@12345
-- Verified Spring Security BCrypt hash for 'Admin@12345': $2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te

INSERT INTO admin_users (email, password_hash, full_name, is_first_login, created_at, updated_at)
VALUES 
    ('admin1@digisevasolution.com', '$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te', 'Partner One', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin2@digisevasolution.com', '$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te', 'Partner Two', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
