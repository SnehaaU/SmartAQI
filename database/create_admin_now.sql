USE smartaqi;

INSERT INTO users (fullname, email, password, role) VALUES
('Admin', 'admin@smartaqi.com', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE password = 'admin123', role = 'admin', fullname = 'Admin';

SELECT id, fullname, email, role FROM users WHERE role = 'admin';
