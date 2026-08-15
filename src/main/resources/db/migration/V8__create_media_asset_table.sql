CREATE TABLE media_assets (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    cloudinary_url VARCHAR(500) NOT NULL,
    cloudinary_public_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NULL,
    file_size_bytes BIGINT NULL,
    uploaded_by BIGINT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_media_assets_type ON media_assets(type);
CREATE INDEX idx_media_assets_uploaded_at ON media_assets(uploaded_at);
