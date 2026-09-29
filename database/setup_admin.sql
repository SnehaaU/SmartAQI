-- SmartAQI: Create admin account (run in MySQL Workbench or CMD)
USE smartaqi;

INSERT INTO users (fullname, email, password, role) VALUES
('Admin', 'admin@smartaqi.com', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE
    fullname = VALUES(fullname),
    password = VALUES(password),
    role = 'admin';

-- Check admin exists:
SELECT id, fullname, email, role FROM users WHERE role = 'admin';
