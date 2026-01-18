package vn.edu.hcmuaf.fit.webdynamic.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

/**
 * Utility class để xử lý logic sidebar
 * - Lấy user từ request hoặc session
 * - Xử lý activeMenu
 * - Xử lý avatarPath
 */
public class SidebarUtil {

    /**
     * Lấy user từ request hoặc session
     * Nếu user không có trong request, sẽ lấy từ session
     */
    public static User getUserFromRequestOrSession(HttpServletRequest request) {
        // Thử lấy từ request trước
        User user = (User) request.getAttribute("user");
        
        // Nếu không có trong request, lấy từ session
        if (user == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                user = (User) session.getAttribute("user");
            }
        }
        
        return user;
    }

    /**
     * Lấy activeMenu từ request
     * Nếu không có, mặc định là "profile"
     */
    public static String getActiveMenu(HttpServletRequest request) {
        String activeMenu = (String) request.getAttribute("activeMenu");
        return activeMenu != null ? activeMenu : "profile";
    }

    /**
     * Tính toán avatar path
     * - Nếu user không có avatar, dùng avatar mặc định
     * - Nếu avatar bắt đầu bằng "/", thêm contextPath
     * - Nếu không, thêm contextPath trước
     */
    public static String getAvatarPath(HttpServletRequest request, User user) {
        if (user == null || user.getAvatar() == null || user.getAvatar().isEmpty()) {
            return request.getContextPath() + "/assert/img/admin.jpg";
        }

        String avatar = user.getAvatar();
        
        if (avatar.startsWith("/")) {
            return request.getContextPath() + avatar;
        } else {
            return request.getContextPath() + "/" + avatar;
        }
    }

    /**
     * Set tất cả thông tin sidebar vào request
     * Hàm tiện lợi để gọi trong servlet
     */
    public static void setSidebarData(HttpServletRequest request) {
        User user = getUserFromRequestOrSession(request);
        String activeMenu = getActiveMenu(request);
        String avatarPath = getAvatarPath(request, user);

        request.setAttribute("user", user);
        request.setAttribute("activeMenu", activeMenu);
        request.setAttribute("avatarPath", avatarPath);
    }
}
