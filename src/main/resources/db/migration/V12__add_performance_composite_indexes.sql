-- Add composite performance indexes for public queries, rate limiting, and OTP lookups

CREATE INDEX IF NOT EXISTS idx_service_items_active_cat_order 
ON service_items(is_active, category, display_order);

CREATE INDEX IF NOT EXISTS idx_content_blocks_status_sec_order 
ON content_blocks(status, section, display_order);

CREATE INDEX IF NOT EXISTS idx_enquiries_phone_created 
ON enquiries(phone, created_at);

CREATE INDEX IF NOT EXISTS idx_otp_tokens_email_verified_created 
ON otp_tokens(email, verified, created_at);
