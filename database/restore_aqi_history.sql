-- Restore aqi_history table if deleted by mistake
USE smartaqi;

CREATE TABLE IF NOT EXISTS aqi_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL,
    aqi_value INT NOT NULL,
    pm25 DECIMAL(10, 2),
    pm10 DECIMAL(10, 2),
    recorded_date DATE NOT NULL
);

-- Delhi - 7 day history
INSERT INTO aqi_history (city_name, aqi_value, pm25, pm10, recorded_date) VALUES
('Delhi', 260, 152.0, 270.0, DATE_SUB(CURDATE(), INTERVAL 6 DAY)),
('Delhi', 270, 158.0, 280.0, DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
('Delhi', 275, 161.0, 285.0, DATE_SUB(CURDATE(), INTERVAL 4 DAY)),
('Delhi', 280, 164.0, 290.0, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
('Delhi', 282, 165.5, 292.0, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
('Delhi', 284, 178.0, 315.0, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
('Delhi', 285, 180.5, 320.2, CURDATE());

-- Mumbai
INSERT INTO aqi_history (city_name, aqi_value, pm25, pm10, recorded_date) VALUES
('Mumbai', 88, 41.0, 72.0, DATE_SUB(CURDATE(), INTERVAL 6 DAY)),
('Mumbai', 90, 42.0, 74.0, DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
('Mumbai', 92, 43.0, 75.0, DATE_SUB(CURDATE(), INTERVAL 4 DAY)),
('Mumbai', 91, 43.5, 76.0, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
('Mumbai', 93, 44.0, 77.0, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
('Mumbai', 94, 44.5, 78.0, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
('Mumbai', 95, 45.0, 78.3, CURDATE());

-- Bangalore
INSERT INTO aqi_history (city_name, aqi_value, pm25, pm10, recorded_date) VALUES
('Bangalore', 35, 10.0, 22.0, DATE_SUB(CURDATE(), INTERVAL 6 DAY)),
('Bangalore', 38, 11.0, 24.0, DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
('Bangalore', 40, 11.5, 25.0, DATE_SUB(CURDATE(), INTERVAL 4 DAY)),
('Bangalore', 39, 12.0, 26.0, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
('Bangalore', 41, 12.2, 27.0, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
('Bangalore', 40, 12.3, 27.5, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
('Bangalore', 42, 12.5, 28.0, CURDATE());

-- Verify
SELECT * FROM aqi_history ORDER BY city_name, recorded_date;
