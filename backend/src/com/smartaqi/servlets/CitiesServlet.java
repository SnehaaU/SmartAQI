package com.smartaqi.servlets;

import com.smartaqi.dao.AQIDAO;
import com.smartaqi.util.BaseServlet;
import com.smartaqi.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/cities")
public class CitiesServlet extends BaseServlet {
    private final AQIDAO aqiDAO = new AQIDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) return;
        try {
            List<String> cities = aqiDAO.getAllCities();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < cities.size(); i++) {
                if (i > 0) json.append(',');
                json.append('"').append(JsonUtil.escape(cities.get(i))).append('"');
            }
            json.append(']');
            sendJson(response, HttpServletResponse.SC_OK, json.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Failed to load cities\"}");
        }
    }
}
