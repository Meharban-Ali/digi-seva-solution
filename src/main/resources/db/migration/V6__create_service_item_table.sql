CREATE TABLE service_items (
    id BIGSERIAL PRIMARY KEY,
    name_en VARCHAR(255) NOT NULL,
    name_hi VARCHAR(255) NULL,
    description_en TEXT NULL,
    description_hi TEXT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NULL,
    image_url VARCHAR(500) NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_service_items_category ON service_items(category);
CREATE INDEX idx_service_items_is_active ON service_items(is_active);
CREATE INDEX idx_service_items_display_order ON service_items(display_order);
