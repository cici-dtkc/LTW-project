package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu từ form và trim khoảng trắng
        String fname = request.getParameter("fname").trim();
        String lname = request.getParameter("lname").trim();
        String email = request.getParameter("email").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        // Kiểm tra rỗng/null
        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || username.isEmpty()
                || password.isEmpty() || confirm.isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ tất cả các trường!");
            request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email hợp lệ
        if (!isValidEmail(email)) {
            request.setAttribute("error", "Email không hợp lệ!");
            request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra confirm password
        if (!password.equals(confirm)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mạnh
        if (!isStrongPassword(password)) {
            request.setAttribute("error", "Mật khẩu phải ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
            request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
            return;
        }

        // Nếu qua hết check UI → gọi Service để xử lý đăng ký
        User newUser = new User(fname, lname, username, password, email);
        boolean success = userService.register(newUser);

        if (success) {
            response.sendRedirect("login?message=register_success");
        } else {
            request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại!");
            request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
        }
    }

    // Hàm kiểm tra email hợp lệ
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    // Hàm kiểm tra mật khẩu mạnh
    private boolean isStrongPassword(String password) {
        // Ít nhất 8 ký tự, có chữ hoa, chữ thường, số, ký tự đặc biệt
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(pattern);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
    }

}