package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "InfoUserServlet", urlPatterns = {"/user/info"})
public class InfoUserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Ở đây giả sử bạn đã lưu userId vào session khi login
        Integer userId = null;
        if (session != null) {
            Object idAttr = session.getAttribute("userId");
            if (idAttr instanceof Integer) {
                userId = (Integer) idAttr;
            } else if (idAttr instanceof String) {
                try {
                    userId = Integer.parseInt((String) idAttr);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (userId == null) {
            // Chưa đăng nhập -> chuyển về trang login (bạn chỉnh lại URL cho đúng)
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Optional<User> userOpt = userService.getUserProfileById(userId);
        if (userOpt.isEmpty()) {
            // Không tìm thấy user -> trả về 404 hoặc redirect
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy người dùng");
            return;
        }

        req.setAttribute("user", userOpt.get());
        req.getRequestDispatcher("/views/user/info-user.jsp").forward(req, resp);
    }
}