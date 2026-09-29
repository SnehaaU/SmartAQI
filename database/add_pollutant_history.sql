-- Add pollutant columns to history (run once in MySQL Workbench)
USE smartaqi;

ALTER TABLE aqi_history
ADD COLUMN pm25 DECIMAL(10, 2) NULL AFTER aqi_value,
ADD COLUMN pm10 DECIMAL(10, 2) NULL AFTER pm25;

-- Update Delhi history with pollutant data (example)
UPDATE aqi_history SET pm25 = 152.0, pm10 = 270.0 WHERE city_name = 'Delhi' AND aqi_value = 260;
UPDATE aqi_history SET pm25 = 158.0, pm10 = 280.0 WHERE city_name = 'Delhi' AND aqi_value = 270;
UPDATE aqi_history SET pm25 = 161.0, pm10 = 285.0 WHERE city_name = 'Delhi' AND aqi_value = 275;
UPDATE aqi_history SET pm25 = 164.0, pm10 = 290.0 WHERE city_name = 'Delhi' AND aqi_value = 280;
UPDATE aqi_history SET pm25 = 165.5, pm10 = 292.0 WHERE city_name = 'Delhi' AND aqi_value = 282;
UPDATE aqi_history SET pm25 = 178.0, pm10 = 315.0 WHERE city_name = 'Delhi' AND aqi_value = 284;
UPDATE aqi_history SET pm25 = 180.5, pm10 = 320.2 WHERE city_name = 'Delhi' AND aqi_value = 285;
