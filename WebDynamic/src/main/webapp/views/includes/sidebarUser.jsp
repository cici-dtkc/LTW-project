<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) request.getAttribute("user");
    // Nếu không có trong request, thử lấy từ session
    if (user == null) {
        HttpSession session1 = request.getSession(false);
        if (session1 != null) {
            user = (User) session1.getAttribute("user");
        }
    }

String activeMenu = (String) request.getAttribute("activeMenu");
if (activeMenu == null) {
    activeMenu = "profile"; // default
}
    String avatarPath;
    if (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty()) {
        String avatar = user.getAvatar();

        // Nếu avatar bắt đầu bằng / (đường dẫn tương đối) → thêm contextPath
        if (avatar.startsWith("/")) {
            avatarPath = request.getContextPath() + avatar;
        }
        // Các trường hợp khác → thêm contextPath
        else {
            avatarPath = request.getContextPath() + "/" + avatar;
        }
    } else {
        // Avatar mặc định
        avatarPath = request.getContextPath() + "/assert/img/admin.jpg";
    }
%>
<html>
<head>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">
<!-- SIDEBAR -->
<div id="accountSidebar">

    <!-- USER INFO -->
    <div id="userBox">
        <img src="<%= avatarPath %>"
             id="userAvatar"
             alt="User Avatar"
             onerror="this.src='${pageContext.request.contextPath}/assert/img/admin.jpg'">

        <div id="userInfo">
            <span id="usernameDisplay"><%= user != null ? user.getUsername() : "Người dùng" %></span>
            <a href="${pageContext.request.contextPath}/user/profile" id="editProfileBtn">
                <i class="fa-solid fa-pen"></i> Sửa Hồ Sơ
            </a>
        </div>
    </div>

    <div class="sidebar-divider"></div>

    <ul id="menuList">

        <!-- TÀI KHOẢN CỦA TÔI -->
        <li class="menu-category">

            <a href="#" id="menuAccountMain">
                <i class="fa-solid fa-user"></i> Tài Khoản Của Tôi
            </a>

            <ul id="accountSubmenu" class="submenu">

                <li class="menu-item <%= "profile".equals(activeMenu) ? "active" : "" %>">
                    <a href="${pageContext.request.contextPath}/user/profile" id="menuProfile">Hồ Sơ</a>
                </li>

                <li class="menu-item <%= "bank".equals(activeMenu) ? "active" : "" %>">
                    <a href="${pageContext.request.contextPath}/user/payment" id="menuBank">Ngân Hàng</a>
                </li>

                <li class="menu-item <%= "address".equals(activeMenu) ? "active" : "" %>">
                    <a href="${pageContext.request.contextPath}/user/addresses" id="menuAddress">Địa Chỉ</a>
                </li>

                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/user/change-password" id="menuPassword">Đổi Mật Khẩu</a>
                </li>
            </ul>

        </li>

        <!-- ĐƠN MUA -->
        <li class="menu-category">
            <a href="${pageContext.request.contextPath}/user/order" id="menuOrders">
                <i class="fa-solid fa-bag-shopping"></i> Đơn Mua
            </a>
        </li>

        <!-- VOUCHER -->
        <li class="menu-category">
            <a href="${pageContext.request.contextPath}/user/voucher-detail" id="menuVoucher">
                <i class="fa-solid fa-ticket"></i> Kho Voucher
            </a>
        </li>

    </ul>

</div>
</body>

</html>
