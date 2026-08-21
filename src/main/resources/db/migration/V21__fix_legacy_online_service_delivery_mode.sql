-- Flyway Migration V21: Fix legacy ONLINE_SERVICE delivery mode string in database
UPDATE service_items 
SET delivery_mode = 'ONLINE' 
WHERE delivery_mode = 'ONLINE_SERVICE';
