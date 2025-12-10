
package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminUserServlet", urlPatterns = { "/admin/users" })
public class AdminUserServlet extends HttpServlet {
    private UserService userService;
    private UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to the admin user management JSP without authentication check
        RequestDispatcher rd = req.getRequestDispatcher("/views/admin/userManagement.jsp");
        rd.forward(req, resp);
    }
    public void init() throws ServletException {
        this.userService = new UserService();
        this.userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        // Authorization: only admin (role==1) allowed
        HttpSession session = req.getSession(false);
        Integer currentUserId = null;
        if (session != null) {
            Object idAttr = session.getAttribute("userId");
            if (idAttr instanceof Integer)
                currentUserId = (Integer) idAttr;
            else if (idAttr instanceof String) {
                try {
                    currentUserId = Integer.parseInt((String) idAttr);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (currentUserId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"success\":false,\"message\":\"Unauthorized\"}");
            }
            return;
        }

        // check role
        User currentUser = userService.getUserProfileById(currentUserId).orElse(null);
        if (currentUser == null || currentUser.getRole() != 1) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"success\":false,\"message\":\"Forbidden\"}");
            }
            return;
        }
    }

}
