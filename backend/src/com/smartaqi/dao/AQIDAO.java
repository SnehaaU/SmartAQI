package com.smartaqi.dao;

import com.smartaqi.DatabaseConnection;
import com.smartaqi.models.AQIData;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AQIDAO {

    public AQIData findByCity(String city) throws SQLException {
        String sql = "SELECT city_name, aqi_value, category, pm25, pm10, health_tip, last_updated " +
                     "FROM aqi_data WHERE LOWER(city_name) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, city.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                AQIData data = new AQIData();
                data.setCityName(rs.getString("city_name"));
                data.setAqiValue(rs.getInt("aqi_value"));
                data.setCategory(rs.getString("category"));
                data.setPm25(rs.getDouble("pm25"));
                data.setPm10(rs.getDouble("pm10"));
                data.setHealthTip(rs.getString("health_tip"));
                if (rs.getDate("last_updated") != null) {
                    data.setLastUpdated(rs.getDate("last_updated").toString());
                }
                loadHistory(conn, data);
                return data;
            }
        }
    }

    public List<AQIData> listAll() throws SQLException {
        List<AQIData> list = new ArrayList<>();
        String sql = "SELECT city_name, aqi_value, category, pm25, pm10, health_tip, last_updated FROM aqi_data ORDER BY city_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AQIData data = new AQIData();
                data.setCityName(rs.getString("city_name"));
                data.setAqiValue(rs.getInt("aqi_value"));
                data.setCategory(rs.getString("category"));
                data.setPm25(rs.getDouble("pm25"));
                data.setPm10(rs.getDouble("pm10"));
                data.setHealthTip(rs.getString("health_tip"));
                if (rs.getDate("last_updated") != null) {
                    data.setLastUpdated(rs.getDate("last_updated").toString());
                }
                list.add(data);
            }
        }
        return list;
    }

    public boolean insert(AQIData data) throws SQLException {
        String sql = "INSERT INTO aqi_data (city_name, aqi_value, category, pm25, pm10, health_tip, last_updated) " +
                     "VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data.getCityName().trim());
            ps.setInt(2, data.getAqiValue());
            ps.setString(3, data.getCategory());
            ps.setDouble(4, data.getPm25());
            ps.setDouble(5, data.getPm10());
            ps.setString(6, data.getHealthTip());
            boolean ok = ps.executeUpdate() == 1;
            if (ok) addHistory(conn, data);
            return ok;
        }
    }

    public boolean update(AQIData data) throws SQLException {
        String sql = "UPDATE aqi_data SET aqi_value=?, category=?, pm25=?, pm10=?, health_tip=?, last_updated=CURDATE() " +
                     "WHERE LOWER(city_name)=LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, data.getAqiValue());
            ps.setString(2, data.getCategory());
            ps.setDouble(3, data.getPm25());
            ps.setDouble(4, data.getPm10());
            ps.setString(5, data.getHealthTip());
            ps.setString(6, data.getCityName().trim());
            boolean ok = ps.executeUpdate() == 1;
            if (ok) addHistory(conn, data);
            return ok;
        }
    }

    public boolean delete(String city) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM aqi_history WHERE LOWER(city_name)=LOWER(?)")) {
                ps.setString(1, city.trim());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM aqi_data WHERE LOWER(city_name)=LOWER(?)")) {
                ps.setString(1, city.trim());
                return ps.executeUpdate() == 1;
            }
        }
    }

    public boolean cityExists(String city) throws SQLException {
        return findByCity(city) != null;
    }

    private void addHistory(Connection conn, AQIData data) throws SQLException {
        String sql = "INSERT INTO aqi_history (city_name, aqi_value, pm25, pm10, recorded_date) " +
                     "VALUES (?, ?, ?, ?, CURDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data.getCityName().trim());
            ps.setInt(2, data.getAqiValue());
            ps.setDouble(3, data.getPm25());
            ps.setDouble(4, data.getPm10());
            ps.executeUpdate();
        }
    }

    public List<String> getAllCities() throws SQLException {
        List<String> cities = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT city_name FROM aqi_data ORDER BY city_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) cities.add(rs.getString("city_name"));
        }
        return cities;
    }

    private void loadHistory(Connection conn, AQIData data) throws SQLException {
        Date endDate = resolveTrendEndDate(data);
        LocalDate end = endDate.toLocalDate();
        data.setTrendStart(end.minusDays(6).toString());
        data.setTrendEnd(end.toString());

        String dateFilter = "AND recorded_date >= DATE_SUB(?, INTERVAL 6 DAY) AND recorded_date <= ? ";
        String sqlWithPm = "SELECT aqi_value, pm25, pm10, recorded_date FROM aqi_history " +
                           "WHERE LOWER(city_name) = LOWER(?) " + dateFilter +
                           "ORDER BY recorded_date ASC";
        String sqlBasic = "SELECT aqi_value, recorded_date FROM aqi_history " +
                          "WHERE LOWER(city_name) = LOWER(?) " + dateFilter +
                          "ORDER BY recorded_date ASC";
        boolean withPm = true;
        try (PreparedStatement ps = conn.prepareStatement(sqlWithPm)) {
            bindHistoryParams(ps, data.getCityName(), endDate);
            try (ResultSet rs = ps.executeQuery()) {
                fillHistory(rs, data, withPm);
            }
        } catch (SQLException ex) {
            withPm = false;
            try (PreparedStatement ps = conn.prepareStatement(sqlBasic)) {
                bindHistoryParams(ps, data.getCityName(), endDate);
                try (ResultSet rs = ps.executeQuery()) {
                    fillHistory(rs, data, withPm);
                }
            }
        }
    }

    private void bindHistoryParams(PreparedStatement ps, String city, Date endDate) throws SQLException {
        ps.setString(1, city);
        ps.setDate(2, endDate);
        ps.setDate(3, endDate);
    }

    private Date resolveTrendEndDate(AQIData data) {
        String lastUpdated = data.getLastUpdated();
        if (lastUpdated != null && !lastUpdated.trim().isEmpty()) {
            try {
                return Date.valueOf(lastUpdated.trim());
            } catch (IllegalArgumentException ignored) {
                // fall through
            }
        }
        return new Date(System.currentTimeMillis());
    }

    private void fillHistory(ResultSet rs, AQIData data, boolean withPmColumns) throws SQLException {
        int currentAqi = data.getAqiValue() > 0 ? data.getAqiValue() : 1;
        while (rs.next()) {
            int histAqi = rs.getInt("aqi_value");
            data.getHistoryValues().add(histAqi);
            data.getHistoryDates().add(rs.getDate("recorded_date").toString());
            double pm25 = data.getPm25() * histAqi / currentAqi;
            double pm10 = data.getPm10() * histAqi / currentAqi;
            if (withPmColumns) {
                if (rs.getObject("pm25") != null) pm25 = rs.getDouble("pm25");
                if (rs.getObject("pm10") != null) pm10 = rs.getDouble("pm10");
            }
            data.getHistoryPm25().add(Math.round(pm25 * 10.0) / 10.0);
            data.getHistoryPm10().add(Math.round(pm10 * 10.0) / 10.0);
        }
        if (data.getHistoryValues().isEmpty()) {
            data.getHistoryValues().add(data.getAqiValue());
            String fallbackDate = data.getTrendEnd() != null ? data.getTrendEnd() : "Today";
            data.getHistoryDates().add(fallbackDate);
            data.getHistoryPm25().add(data.getPm25());
            data.getHistoryPm10().add(data.getPm10());
        }
    }
}
