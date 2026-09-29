package com.smartaqi.servlets;

import com.smartaqi.dao.AQIDAO;
import com.smartaqi.models.AQIData;
import com.smartaqi.util.AQIUtil;
import com.smartaqi.util.BaseServlet;
import com.smartaqi.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/admin/aqi")
public class AdminAQIServlet extends BaseServlet {
    private final AQIDAO aqiDAO = new AQIDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireAdmin(request, response)) return;
        try {
            List<AQIData> list = aqiDAO.listAll();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) json.append(',');
                json.append(toJson(list.get(i)));
            }
            json.append(']');
            sendJson(response, HttpServletResponse.SC_OK, json.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"" + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireAdmin(request, response)) return;
        try {
            AQIData data = parseBody(readBody(request));
            if (data == null) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Invalid AQI data\"}");
                return;
            }
            if (data.getCityName().isEmpty()) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"City name is required\"}");
                return;
            }
            if (aqiDAO.cityExists(data.getCityName())) {
                sendJson(response, HttpServletResponse.SC_CONFLICT, "{\"error\":\"City already exists\"}");
                return;
            }
            if (aqiDAO.insert(data)) {
                sendJson(response, HttpServletResponse.SC_OK, "{\"success\":true,\"message\":\"AQI added\"}");
            } else {
                sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Failed to add AQI\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"" + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireAdmin(request, response)) return;
        try {
            AQIData data = parseBody(readBody(request));
            if (data == null) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Invalid AQI data\"}");
                return;
            }
            if (data.getCityName().isEmpty()) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"City name is required\"}");
                return;
            }
            if (!aqiDAO.cityExists(data.getCityName())) {
                sendJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"City not found\"}");
                return;
            }
            if (aqiDAO.update(data)) {
                sendJson(response, HttpServletResponse.SC_OK, "{\"success\":true,\"message\":\"AQI updated\"}");
            } else {
                sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Failed to update AQI\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"" + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireAdmin(request, response)) return;
        String city = request.getParameter("city");
        if (city == null || city.trim().isEmpty()) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"City parameter required\"}");
            return;
        }
        try {
            if (aqiDAO.delete(city)) {
                sendJson(response, HttpServletResponse.SC_OK, "{\"success\":true,\"message\":\"AQI deleted\"}");
            } else {
                sendJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"City not found\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"" + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }

    private AQIData parseBody(String body) {
        try {
            AQIData data = new AQIData();
            data.setCityName(JsonUtil.readJsonField(body, "city_name"));
            String aqiStr = JsonUtil.readJsonField(body, "aqi_value");
            int aqi = aqiStr.isEmpty() ? 0 : Integer.parseInt(aqiStr);
            data.setAqiValue(aqi);
            String category = JsonUtil.readJsonField(body, "category");
            data.setCategory(category.isEmpty() ? AQIUtil.getCategory(aqi) : category);
            String pm25 = JsonUtil.readJsonField(body, "pm25");
            String pm10 = JsonUtil.readJsonField(body, "pm10");
            data.setPm25(pm25.isEmpty() ? 0 : Double.parseDouble(pm25));
            data.setPm10(pm10.isEmpty() ? 0 : Double.parseDouble(pm10));
            data.setHealthTip(JsonUtil.readJsonField(body, "health_tip"));
            return data;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String toJson(AQIData d) {
        return String.format(
            "{\"city_name\":\"%s\",\"aqi_value\":%d,\"category\":\"%s\",\"pm25\":%.1f,\"pm10\":%.1f," +
            "\"health_tip\":\"%s\",\"last_updated\":\"%s\"}",
            JsonUtil.escape(d.getCityName()), d.getAqiValue(), JsonUtil.escape(d.getCategory()),
            d.getPm25(), d.getPm10(), JsonUtil.escape(d.getHealthTip()),
            JsonUtil.escape(d.getLastUpdated() != null ? d.getLastUpdated() : "")
        );
    }
}
