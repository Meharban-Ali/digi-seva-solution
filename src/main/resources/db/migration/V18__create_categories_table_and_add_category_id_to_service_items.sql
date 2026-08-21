-- Migration V18: Create categories table, seed 19 categories, add category_id to service_items
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name_en VARCHAR(255) NOT NULL,
    name_hi VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    icon VARCHAR(100) DEFAULT 'Folder',
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_categories_active_order ON categories(is_active, display_order);

ALTER TABLE service_items ADD COLUMN category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL;
CREATE INDEX idx_service_items_category_id ON service_items(category_id);

-- Seed 19 real top-level categories
INSERT INTO categories (name_en, name_hi, slug, icon, display_order, is_active) VALUES
('Government Services', 'सरकारी सेवाएं', 'government-services', 'ShieldCheck', 1, TRUE),
('Banking Services', 'बैंकिंग सेवाएं', 'banking-services', 'Building2', 2, TRUE),
('Utility Services', 'उपयोगिता सेवाएं', 'utility-services', 'Zap', 3, TRUE),
('Tax Services', 'कर समस्याएं व सेवाएं', 'tax-services', 'Receipt', 4, TRUE),
('Business Registration', 'व्यवसाय पंजीकरण', 'business-registration', 'Briefcase', 5, TRUE),
('Digital Signature', 'डिजिटल सिग्नेचर', 'digital-signature', 'FileCheck', 6, TRUE),
('Travel Services', 'यात्रा सेवाएं', 'travel-services', 'Plane', 7, TRUE),
('Education Services', 'शिक्षा सेवाएं', 'education-services', 'GraduationCap', 8, TRUE),
('Job Services', 'नौकरी सेवाएं', 'job-services', 'UserCheck', 9, TRUE),
('Cyber Cafe Services', 'साइबर कैफे सेवाएं', 'cyber-cafe-services', 'Monitor', 10, TRUE),
('Insurance', 'बीमा सेवाएं', 'insurance', 'Shield', 11, TRUE),
('Financial Services', 'वित्तीय सेवाएं', 'financial-services', 'Landmark', 12, TRUE),
('Property Services', 'संपत्ति संबंधी सेवाएं', 'property-services', 'Home', 13, TRUE),
('Legal Services', 'कानूनी सेवाएं', 'legal-services', 'Scale', 14, TRUE),
('Courier & Logistics', 'कोरियर और लॉजिस्टिक्स', 'courier-logistics', 'Truck', 15, TRUE),
('E-Commerce Services', 'ई-कॉमर्स सेवाएं', 'ecommerce-services', 'ShoppingBag', 16, TRUE),
('Social Media Services', 'सोशल मीडिया सेवाएं', 'social-media-services', 'Share2', 17, TRUE),
('AI Services', 'एआई सेवाएं', 'ai-services', 'Cpu', 18, TRUE),
('Java Full Stack & IT Services', 'जावा फुल स्टैक एवं आईटी सेवाएं', 'java-it-services', 'Code', 19, TRUE);
