ALTER TABLE service_items
ADD COLUMN is_featured BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_service_items_is_featured ON service_items(is_featured);
