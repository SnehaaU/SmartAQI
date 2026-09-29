-- Fix: add missing 'role' column (required for register & admin login)
USE smartaqi;

ALTER TABLE users
ADD COLUMN role ENUM('user', 'admin') NOT NULL DEFAULT 'user' AFTER password;

-- Verify
DESCRIBE users;
SELECT id, fullname, email, role FROM users;
