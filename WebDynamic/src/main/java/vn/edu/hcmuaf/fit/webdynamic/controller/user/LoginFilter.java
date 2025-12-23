package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/cart", "/checkout", "/profile", "/user/*"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);

        // Nếu CHƯA LOGIN
        if (session == null || session.getAttribute("user") == null) {

            // Lưu lại đường dẫn người dùng đang cố vào
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();


            String fullUrl = requestURI + (queryString != null ? "?" + queryString : "");

            request.getSession(true).setAttribute("redirectUrl", fullUrl);


            request.getRequestDispatcher("/login").forward(request, servletResponse);
            return;
        }

        // ✔ Đã login → cho đi tiếp
        chain.doFilter(servletRequest, servletResponse);
    }
}
