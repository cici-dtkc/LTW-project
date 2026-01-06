package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.http.HttpServlet;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String input = request.getParameter("input");
        String password = request.getParameter("password");

        User user = userService.login(input, password);

        if (user == null) {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("inputValue", input);
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole());

        int role = user.getRole();

        // Trả về trang yêu cầu login trước đó
        String redirectUrl = (String) session.getAttribute("redirectUrl");
        if (redirectUrl != null) {
            session.removeAttribute("redirectUrl");
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }

        if (role == 0) { // admin
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else { // user
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
