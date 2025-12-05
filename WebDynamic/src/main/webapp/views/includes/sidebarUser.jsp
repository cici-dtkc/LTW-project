<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
</head>
<body>
<!-- SIDEBAR -->
<div id="accountSidebar">

    <!-- USER INFO -->
    <div id="userBox">
        <img src="https://cdn-icons-png.flaticon.com/512/847/847969.png" id="userAvatar">

        <div id="userInfo">
            <span id="usernameDisplay">user1</span>
            <a href="info-user.html" id="editProfileBtn">
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

                <li class="menu-item">
                    <a href="info-user.html" id="menuProfile">Hồ Sơ</a>
                </li>

                <li class="menu-item active">
                    <a href="paymentForm.html" id="menuBank">Ngân Hàng</a>
                </li>

                <li class="menu-item">
                    <a href="addresses.html" id="menuAddress">Địa Chỉ</a>
                </li>

                <li class="menu-item">
                    <a href="newPassword.html" id="menuPassword">Đổi Mật Khẩu</a>
                </li>
            </ul>

        </li>

        <!-- ĐƠN MUA -->
        <li class="menu-category">
            <a href="order.html" id="menuOrders">
                <i class="fa-solid fa-bag-shopping"></i> Đơn Mua
            </a>
        </li>

        <!-- VOUCHER -->
        <li class="menu-category">
            <a href="voucherDetail.html" id="menuVoucher">
                <i class="fa-solid fa-ticket"></i> Kho Voucher
            </a>
        </li>

    </ul>

</div>
</body>
<script src="${pageContext.request.contextPath}/js/info-user.js"></script>
</html>
