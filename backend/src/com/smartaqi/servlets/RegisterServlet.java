package com.smartaqi.servlets;

import com.smartaqi.dao.UserDAO;
import com.smartaqi.models.User;
import com.smartaqi.util.BaseServlet;
import com.smartaqi.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/api/register")
public class RegisterServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String body = request.getReader().lines().collect(Collectors.joining());
            String fullname = JsonUtil.readJsonField(body, "fullname");
            String email = JsonUtil.readJsonField(body, "email");
            String password = JsonUtil.readJsonField(body, "password");

            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"error\":\"All fields are required\"}");
                return;
            }
            if (password.length() < 4) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"error\":\"Password must be at least 4 characters\"}");
                return;
            }
            if (userDAO.emailExists(email)) {
                sendJson(response, HttpServletResponse.SC_CONFLICT,
                    "{\"error\":\"Email already registered\"}");
                return;
            }

            if (userDAO.register(new User(fullname, email, password))) {
                sendJson(response, HttpServletResponse.SC_OK,
                    "{\"success\":true,\"message\":\"Registration successful\"}");
            } else {
                sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "{\"error\":\"Registration failed\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"Database error: " + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }
}
