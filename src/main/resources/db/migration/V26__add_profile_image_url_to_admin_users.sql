-- Flyway Migration V26: Add profile_image_url column to admin_users table
ALTER TABLE admin_users ADD COLUMN profile_image_url VARCHAR(500);
