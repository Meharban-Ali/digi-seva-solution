-- Flyway Migration V24: Seed default published WELCOME_POPUP content block
INSERT INTO content_blocks (section, title_en, title_hi, body_en, body_hi, status, display_order)
VALUES (
    'WELCOME_POPUP',
    'Welcome to Digi Seva Solution',
    'डिजी सेवा सॉल्यूशन में आपका स्वागत है',
    'Authorized Common Service Center (CSC) & Digital Solutions Partner in New Ashok Nagar, Delhi. We provide fast Jan Seva Kendra government services, banking, utility payments, and custom software development.',
    'न्यू अशोक नगर, दिल्ली में अधिकृत जन सेवा केंद्र (CSC) एवं डिजिटल सॉल्यूशंस पार्टनर। हम तेज सरकारी सेवाएं, बैंकिंग, बिल भुगतान और कस्टम सॉफ्टवेयर डेवलपमेंट प्रदान करते हैं।',
    'PUBLISHED',
    5
);
