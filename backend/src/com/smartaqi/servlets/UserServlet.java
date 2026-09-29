package com.smartaqi.servlets;

import com.smartaqi.util.BaseServlet;
import com.smartaqi.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/api/user")
public class UserServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendJson(response, HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"Not logged in\"}");
            return;
        }

        String json = String.format(
            "{\"fullname\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
            JsonUtil.escape((String) session.getAttribute("fullname")),
            JsonUtil.escape((String) session.getAttribute("email")),
            JsonUtil.escape((String) session.getAttribute("role"))
        );
        sendJson(response, HttpServletResponse.SC_OK, json);
    }
}
