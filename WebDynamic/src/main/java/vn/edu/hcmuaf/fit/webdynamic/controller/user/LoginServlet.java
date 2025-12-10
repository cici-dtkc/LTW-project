package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.http.HttpServlet;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String input = request.getParameter("input");
        String password = request.getParameter("password");

        UserDao dao = new UserDao();
        User user = dao.login(input, password);

        // ❌ Sai mật khẩu hoặc tài khoản
        if (user == null) {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("inputValue", input);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }


        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole());

        int role = user.getRole();

        // ⚡ 1. User truy cập trang yêu cầu login → trả nó về đúng trang đó
        String redirectUrl = (String) session.getAttribute("redirectUrl");
        if (redirectUrl != null) {
            session.removeAttribute("redirectUrl");

            // Chỉ user thường mới được truy cập trang user
            if (role == 0) {
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }
        }

        // ⚡ 2. Nếu không → điều hướng theo role
        if (role == 1) { // admin
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        } else { // user
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
