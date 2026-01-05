package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.io.IOException;

@WebFilter(urlPatterns = {"/cart", "/checkout", "/profile", "/user/*", "/admin/*"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Nếu CHƯA LOGIN
        if (session == null || session.getAttribute("user") == null) {

            // Lưu lại đường dẫn người dùng đang cố vào
            String queryString = request.getQueryString();
            String fullUrl = requestURI + (queryString != null ? "?" + queryString : "");
            request.getSession(true).setAttribute("redirectUrl", fullUrl);

            // Chuyển hướng sang trang login
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        // ✔ Đã login → kiểm tra role
        User user = (User) session.getAttribute("user"); // Giả sử bạn có class User
        int role = user.getRole(); // 0 = admin, 1 = user

        // Kiểm tra phân quyền
        if (requestURI.startsWith(contextPath + "/admin") && role != 0) {
            // User thường không vào admin được
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này!");
            return;
        }

        if ((requestURI.startsWith(contextPath + "/cart")
                || requestURI.startsWith(contextPath + "/user")
                || requestURI.startsWith(contextPath + "/checkout")) && role != 1) {
            // Admin không vào trang user
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này!");
            return;
        }

        // ✔ Được phép → tiếp tục
        chain.doFilter(servletRequest, servletResponse);
    }
}
