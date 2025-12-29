package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangepassServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("form_change_pass.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            resp.sendRedirect("/login");
            return;
        }

        int userId = currentUser.getId();
        String newPass = req.getParameter("new_password");
        String confirm = req.getParameter("confirm_password");

        // 1. Kiểm tra confirm password
        if (!newPass.equals(confirm)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("form_change_pass.jsp").forward(req, resp);
            return;
        }

        String result = userService.updatePassword(userId, newPass);

        // 3. Kết quả
        if (result.equals("Đổi mật khẩu thành công")) {
            req.setAttribute("message", result);
        } else {
            req.setAttribute("error", result);
        }

        req.getRequestDispatcher("form_change_pass.jsp").forward(req, resp);
    }

}
