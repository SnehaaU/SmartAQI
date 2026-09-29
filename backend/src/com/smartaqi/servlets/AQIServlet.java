package com.smartaqi.servlets;

import com.smartaqi.dao.AQIDAO;
import com.smartaqi.models.AQIData;
import com.smartaqi.util.BaseServlet;
import com.smartaqi.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/aqi")
public class AQIServlet extends BaseServlet {
    private final AQIDAO aqiDAO = new AQIDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) return;

        String city = request.getParameter("city");
        if (city == null || city.trim().isEmpty()) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"City is required\"}");
            return;
        }

        try {
            AQIData data = aqiDAO.findByCity(city);
            if (data == null) {
                sendJson(response, HttpServletResponse.SC_OK, "{\"error\":\"City not found in database\"}");
                return;
            }
            sendJson(response, HttpServletResponse.SC_OK, toJson(data));
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Database error\"}");
        }
    }

    private String toJson(AQIData d) {
        StringBuilder hv = new StringBuilder("[");
        for (int i = 0; i < d.getHistoryValues().size(); i++) {
            if (i > 0) hv.append(',');
            hv.append(d.getHistoryValues().get(i));
        }
        hv.append(']');

        StringBuilder hd = new StringBuilder("[");
        for (int i = 0; i < d.getHistoryDates().size(); i++) {
            if (i > 0) hd.append(',');
            hd.append('"').append(JsonUtil.escape(d.getHistoryDates().get(i))).append('"');
        }
        hd.append(']');

        StringBuilder hp25 = new StringBuilder("[");
        for (int i = 0; i < d.getHistoryPm25().size(); i++) {
            if (i > 0) hp25.append(',');
            hp25.append(d.getHistoryPm25().get(i));
        }
        hp25.append(']');

        StringBuilder hp10 = new StringBuilder("[");
        for (int i = 0; i < d.getHistoryPm10().size(); i++) {
            if (i > 0) hp10.append(',');
            hp10.append(d.getHistoryPm10().get(i));
        }
        hp10.append(']');

        return String.format(
            "{\"city_name\":\"%s\",\"aqi_value\":%d,\"category\":\"%s\",\"pm25\":%.1f,\"pm10\":%.1f," +
            "\"health_tip\":\"%s\",\"last_updated\":\"%s\",\"trend_start\":\"%s\",\"trend_end\":\"%s\"," +
            "\"history_values\":%s,\"history_dates\":%s,\"history_pm25\":%s,\"history_pm10\":%s}",
            JsonUtil.escape(d.getCityName()), d.getAqiValue(), JsonUtil.escape(d.getCategory()),
            d.getPm25(), d.getPm10(), JsonUtil.escape(d.getHealthTip()),
            JsonUtil.escape(d.getLastUpdated() != null ? d.getLastUpdated() : ""),
            JsonUtil.escape(d.getTrendStart() != null ? d.getTrendStart() : ""),
            JsonUtil.escape(d.getTrendEnd() != null ? d.getTrendEnd() : ""),
            hv, hd, hp25, hp10
        );
    }
}
