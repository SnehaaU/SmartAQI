-- SmartAQI - Run in MySQL 8.0+
CREATE DATABASE IF NOT EXISTS smartaqi;
USE smartaqi;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('user', 'admin') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS aqi_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL UNIQUE,
    aqi_value INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    pm25 DECIMAL(10, 2),
    pm10 DECIMAL(10, 2),
    health_tip VARCHAR(255),
    last_updated DATE
);

CREATE TABLE IF NOT EXISTS aqi_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL,
    aqi_value INT NOT NULL,
    pm25 DECIMAL(10, 2),
    pm10 DECIMAL(10, 2),
    recorded_date DATE NOT NULL
);

-- Admin account: edit database/setup_admin.sql with YOUR email/password, then run that file.
-- (Do not use a normal user account on admin.html — only rows with role = 'admin' work.)

INSERT INTO aqi_data (city_name, aqi_value, category, pm25, pm10, health_tip, last_updated) VALUES
('Delhi', 285, 'Hazardous', 180.5, 320.2, 'Avoid outdoor activities. Stay indoors.', CURDATE()),
('Mumbai', 95, 'Moderate', 45.0, 78.3, 'Sensitive people should limit outdoor exertion.', CURDATE()),
('Bangalore', 42, 'Good', 12.5, 28.0, 'Excellent air quality! Enjoy outdoor activities.', CURDATE()),
('Chennai', 78, 'Moderate', 35.2, 62.1, 'Air quality is acceptable for most people.', CURDATE()),
('Kolkata', 165, 'Poor', 95.0, 145.8, 'Wear a mask when going out.', CURDATE())
ON DUPLICATE KEY UPDATE aqi_value = VALUES(aqi_value);

INSERT INTO aqi_history (city_name, aqi_value, pm25, pm10, recorded_date) VALUES
('Delhi', 260, 152.0, 270.0, DATE_SUB(CURDATE(), INTERVAL 6 DAY)),
('Delhi', 270, 158.0, 280.0, DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
('Delhi', 275, 161.0, 285.0, DATE_SUB(CURDATE(), INTERVAL 4 DAY)),
('Delhi', 280, 164.0, 290.0, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
('Delhi', 282, 165.5, 292.0, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
('Delhi', 284, 178.0, 315.0, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
('Delhi', 285, 180.5, 320.2, CURDATE());
