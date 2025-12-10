package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import  vn.edu.hcmuaf.fit.webdynamic.util.HashPasswordUtil;

import java.io.IOException;
@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        // 1. Kiểm tra password xác nhận
        if (!password.equals(confirm)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 2. Kiểm tra username có tồn tại chưa
        if (UserDao.getInstance().checkExistUsername(username)) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 3. Kiểm tra email có tồn tại chưa
        if (UserDao.getInstance().checkExistEmail(email)) {
            request.setAttribute("error", "Email đã được sử dụng!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 4. Lưu user
//        String hashPass = HashPasswordUtil.hashPassword(password);

        User user = new User(fname, lname, username, password, email);

        boolean success = UserDao.getInstance().register(user);

        if (success) {
            response.sendRedirect("login?message=register_success");
        } else {
            request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }

    }
}