package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
//
//        if (session != null) {
//            session.invalidate();
//        }
//
//        resp.sendRedirect(req.getContextPath() + "/home");
        // Lấy URL hiện tại từ referer hoặc parameter
        String referer = request.getHeader("Referer");
        String currentUrl = request.getParameter("redirect");

        // Ưu tiên parameter, nếu không có thì dùng referer
        String previousUrl = (currentUrl != null && !currentUrl.isEmpty())
                ? currentUrl
                : (referer != null ? referer : null);

        // Invalidate session
        if (session != null) {
            session.invalidate();
        }

        String contextPath = request.getContextPath();
        String redirectUrl;

        // Nếu không có URL trước đó, mặc định về home
        if (previousUrl == null || previousUrl.isEmpty()) {
            redirectUrl = contextPath + "/home";
        } else {
            // Trích xuất path từ URL (có thể là URL đầy đủ hoặc relative path)
            String relativeUrl = extractRelativePath(previousUrl, contextPath);

            // Nếu không thể trích xuất path hợp lệ, về home
            if (relativeUrl == null || relativeUrl.isEmpty() || !relativeUrl.startsWith("/")) {
                redirectUrl = contextPath + "/home";
            } else {
                // Loại bỏ query string để kiểm tra path
                String pathWithoutQuery = relativeUrl.split("\\?")[0];

                // Kiểm tra xem trang có bắt buộc đăng nhập không
                // Các trang bắt buộc đăng nhập: /cart, /checkout, /profile, /user/*, /admin/*
                boolean requiresLogin = pathWithoutQuery.startsWith("/cart")
                        || pathWithoutQuery.startsWith("/checkout")
                        || pathWithoutQuery.startsWith("/profile")
                        || pathWithoutQuery.startsWith("/user/")
                        || pathWithoutQuery.startsWith("/admin/");

                if (requiresLogin) {
                    // Trang bắt buộc đăng nhập
                    if (pathWithoutQuery.startsWith("/admin/")) {
                        // Trang admin -> về login
                        redirectUrl = contextPath + "/login";
                    } else {
                        // Trang user bắt buộc đăng nhập -> về home
                        redirectUrl = contextPath + "/home";
                    }
                } else {
                    // Trang không bắt buộc đăng nhập -> giữ nguyên trang (bao gồm query string nếu có)
                    redirectUrl = contextPath + relativeUrl;
                }
            }
        }

        response.sendRedirect(redirectUrl);
    }

    /**
     * Trích xuất relative path từ URL (có thể là URL đầy đủ hoặc relative path)
     */
    private String extractRelativePath(String url, String contextPath) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        try {
            // Nếu là URL đầy đủ (http:// hoặc https://)
            if (url.startsWith("http://") || url.startsWith("https://")) {
                java.net.URL urlObj = new java.net.URL(url);
                String path = urlObj.getPath();

                // Loại bỏ context path nếu có
                if (path.startsWith(contextPath)) {
                    return path.substring(contextPath.length());
                }
                return path;
            }

            // Nếu đã chứa context path, loại bỏ nó
            if (url.startsWith(contextPath)) {
                return url.substring(contextPath.length());
            }

            // Nếu đã là relative path, trả về nguyên
            if (url.startsWith("/")) {
                return url;
            }

            // Trường hợp khác, thêm / ở đầu
            return "/" + url;
        } catch (Exception e) {
            // Nếu có lỗi parse URL, thử cách đơn giản hơn
            if (url.contains(contextPath)) {
                int index = url.indexOf(contextPath);
                return url.substring(index + contextPath.length());
            }
            return url.startsWith("/") ? url : "/" + url;
        }
    }
}
