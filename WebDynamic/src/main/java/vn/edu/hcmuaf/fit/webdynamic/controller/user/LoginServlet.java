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
        String input = request.getParameter("input");      // email hoặc username
        String password = request.getParameter("password");
        System.out.println("INPUT = " + input);
        System.out.println("PASSWORD = " + password);


        UserDao dao = new UserDao();
        User user = dao.login(input, password);
        if (user == null) {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("inputValue", input);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // Lưu user vào session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Điều hướng theo role
            int role = user.getRole();   // nhớ getRole() phải tồn tại trong model

            if (role == 0) {
                response.sendRedirect("home");
            } else if (role == 1) {
                response.sendRedirect("admin");
            } else {
                // Nếu có role khác thì đưa về trang mặc định
                response.sendRedirect("home");
            }
        }
    }



}
