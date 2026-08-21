-- Migration V17: Rename mislabeled category column to delivery_mode
ALTER TABLE service_items RENAME COLUMN category TO delivery_mode;
ALTER INDEX idx_service_items_category RENAME TO idx_service_items_delivery_mode;
