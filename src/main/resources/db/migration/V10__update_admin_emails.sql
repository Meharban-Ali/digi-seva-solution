-- Update seeded admin email addresses to real partner email addresses
-- Preserves existing password hashes and first-time login states

UPDATE admin_users
SET email = 'pashamr303@gmail.com',
    updated_at = CURRENT_TIMESTAMP
WHERE email = 'admin1@digisevasolution.com';

UPDATE admin_users
SET email = 'sahanealam07860@gmail.com',
    updated_at = CURRENT_TIMESTAMP
WHERE email = 'admin2@digisevasolution.com';
