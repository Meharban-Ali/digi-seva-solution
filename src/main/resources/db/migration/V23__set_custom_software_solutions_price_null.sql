-- Flyway Migration V23: Set Custom Software Solutions price to NULL
-- Pricing must come from admin-managed entries, default to NULL for Contact for Pricing
UPDATE service_items 
SET price = NULL 
WHERE name_en = 'Custom Software Solutions' OR name_en = 'custom-software-solutions';
