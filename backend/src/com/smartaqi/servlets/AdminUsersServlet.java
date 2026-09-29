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
import java.util.List;

@WebServlet("/api/admin/users")
public class AdminUsersServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireAdmin(request, response)) return;
        try {
            List<User> users = userDAO.listAll();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < users.size(); i++) {
                if (i > 0) json.append(',');
                User u = users.get(i);
                json.append(String.format(
                    "{\"id\":%d,\"fullname\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
                    u.getId(), JsonUtil.escape(u.getFullname()),
                    JsonUtil.escape(u.getEmail()), JsonUtil.escape(u.getRole())
                ));
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireAdmin(request, response)) return;
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"User id required\"}");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            HttpSession session = request.getSession(false);
            int currentId = (Integer) session.getAttribute("userId");

            if (id == currentId) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Cannot delete your own account\"}");
                return;
            }

            User target = userDAO.findById(id);
            if (target == null) {
                sendJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"User not found\"}");
                return;
            }
            if ("admin".equals(target.getRole())) {
                sendJson(response, HttpServletResponse.SC_FORBIDDEN, "{\"error\":\"Cannot delete admin accounts\"}");
                return;
            }

            if (userDAO.deleteById(id)) {
                sendJson(response, HttpServletResponse.SC_OK, "{\"success\":true,\"message\":\"User deleted\"}");
            } else {
                sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Failed to delete user\"}");
            }
        } catch (NumberFormatException e) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Invalid user id\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "{\"error\":\"" + JsonUtil.escape(e.getMessage()) + "\"}");
        }
    }
}
