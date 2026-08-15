CREATE TABLE content_blocks (
    id BIGSERIAL PRIMARY KEY,
    section VARCHAR(50) NOT NULL,
    title_en VARCHAR(255) NOT NULL,
    title_hi VARCHAR(255) NULL,
    body_en TEXT NOT NULL,
    body_hi TEXT NULL,
    linked_media_id BIGINT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_content_blocks_section ON content_blocks(section);
CREATE INDEX idx_content_blocks_status ON content_blocks(status);
CREATE INDEX idx_content_blocks_display_order ON content_blocks(display_order);
