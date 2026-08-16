-- Migration V14: Convert otp_tokens timestamps to TIMESTAMPTZ and truncate stale rate-limit records
ALTER TABLE otp_tokens ALTER COLUMN expires_at TYPE TIMESTAMPTZ USING expires_at AT TIME ZONE 'UTC';
ALTER TABLE otp_tokens ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'UTC';

-- Truncate stale OTP records created with timezone-naive local wall-clock values
TRUNCATE TABLE otp_tokens;
