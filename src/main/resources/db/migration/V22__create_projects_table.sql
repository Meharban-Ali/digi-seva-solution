-- Flyway Migration V22: Create projects table for portfolio showcase
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    title_en VARCHAR(255) NOT NULL,
    title_hi VARCHAR(255) NULL,
    description_en TEXT NULL,
    description_hi TEXT NULL,
    image_url VARCHAR(500) NULL,
    project_url VARCHAR(500) NULL,
    category_tag VARCHAR(100) NULL,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_projects_active_featured ON projects(is_active, is_featured);
