package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUserServlet", urlPatterns = { "/admin/users" })
public class AdminUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    /**
     * GET -> Load danh sách user
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<User> users = userService.getAllUsers();

        // Nếu gọi từ AJAX dataTable thì trả JSON
        String ajax = req.getParameter("ajax");
        if ("1".equals(ajax)) {
            resp.setContentType("application/json; charset=UTF-8");

            StringBuilder sb = new StringBuilder();
            sb.append("[");

            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                sb.append("{")
                        .append("\"id\":").append(u.getId()).append(",")
                        .append("\"fullname\":\"").append(u.getFirstName()).append(" ").append(u.getLastName()).append("\",")
                        .append("\"email\":\"").append(u.getEmail()).append("\",")
                        .append("\"role\":").append(u.getRole()).append(",")
                        .append("\"status\":").append(u.getStatus())
                        .append("}");
                if (i < users.size() - 1) sb.append(",");
            }

            sb.append("]");
            resp.getWriter().write(sb.toString());
            return;
        }

        // Nếu không phải AJAX -> forward trang JSP
        req.setAttribute("users", users);
        RequestDispatcher rd = req.getRequestDispatcher("/views/admin/userManagement.jsp");
        rd.forward(req, resp);
    }

    /**
     * POST -> Update role & status
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        int role = Integer.parseInt(req.getParameter("role"));
        int status = Integer.parseInt(req.getParameter("status"));

        boolean ok = userService.updateUser(id, role, status);

        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write("{\"success\": " + ok + "}");
    }
}
