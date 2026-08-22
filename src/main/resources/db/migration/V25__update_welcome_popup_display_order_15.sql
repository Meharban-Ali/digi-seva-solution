-- Flyway Migration V25: Update default WELCOME_POPUP display_order to 15 seconds
UPDATE content_blocks
SET display_order = 15
WHERE section = 'WELCOME_POPUP';
