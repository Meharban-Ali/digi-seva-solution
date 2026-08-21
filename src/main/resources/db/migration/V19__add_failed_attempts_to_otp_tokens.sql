-- Flyway Migration V19: Add failed_attempts column to otp_tokens table for brute-force protection
ALTER TABLE otp_tokens 
ADD COLUMN IF NOT EXISTS failed_attempts INT NOT NULL DEFAULT 0;
