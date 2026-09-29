package com.smartaqi.servlets;

import com.smartaqi.dao.UserDAO;
import com.smartaqi.models.User;
import com.smartaqi.util.BaseServlet;
import com.smartaqi.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/api/login")
public class LoginServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String body = request.getReader().lines().collect(Collectors.joining());
            String email = JsonUtil.readJsonField(body, "email");
            String password = JsonUtil.readJsonField(body, "password");

            if (email.isEmpty() || password.isEmpty()) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"error\":\"Email and password are required\"}");
                return;
            }

            User user = userDAO.findByEmail(email);
            if (user == null || !user.getPassword().equals(password)) {
                sendJson(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"error\":\"Invalid email or password\"}");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("fullname", user.getFullname());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getRole());

            String json = String.format(
                "{\"success\":true,\"fullname\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
                JsonUtil.escape(user.getFullname()),
                JsonUtil.escape(user.getEmail()),
                JsonUtil.escape(user.getRole())
            );
            sendJson(response, HttpServletResponse.SC_OK, json);
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"Database error: " + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }
}
