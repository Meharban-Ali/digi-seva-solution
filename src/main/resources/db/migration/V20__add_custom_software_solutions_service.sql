-- Flyway Migration V20: Add Custom Software Solutions IT Service
UPDATE service_items SET delivery_mode = 'ONLINE' WHERE delivery_mode = 'ONLINE_SERVICE';

INSERT INTO service_items (
    name_en, name_hi, 
    description_en, description_hi, 
    delivery_mode, price, 
    image_url, is_active, 
    display_order, is_featured, 
    category_id, created_at, updated_at
) VALUES (
    'Custom Software Solutions', 
    'कस्टम सॉफ्टवेयर समाधान',
    'Custom business automation tools, internal software systems, database management, and enterprise IT consulting tailored for business requirements.',
    'व्यापार स्वचालन उपकरण, आंतरिक सॉफ्टवेयर प्रणाली, डेटाबेस प्रबंधन, और उद्यम आईटी समाधान।',
    'ONLINE',
    14999.00,
    'https://images.unsplash.com/photo-1555066931-4365d14bab8c?q=80&w=1000&auto=format&fit=crop',
    TRUE,
    20,
    TRUE,
    (SELECT id FROM categories WHERE slug = 'java-it-services' LIMIT 1),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
