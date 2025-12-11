<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Form</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WebStatic/assert/css/accountSidebar.css">
</head>
<body>



    <!-- SIDEBAR -->
    <div id="accountSidebar">

        <!-- USER INFO -->
        <div id="userBox">
            <img src="https://cdn-icons-png.flaticon.com/512/847/847969.png" id="userAvatar">

            <div id="userInfo">
                <span id="usernameDisplay">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            ${sessionScope.user.username}
                        </c:when>
                        <c:otherwise>
                            user1
                        </c:otherwise>
                    </c:choose>
                </span>
                <a href="${pageContext.request.contextPath}/WebStatic/info-user.jsp" id="editProfileBtn">
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

                <!-- ✅ submenu phải nằm trong LI -->
                <ul id="accountSubmenu" class="submenu">

                    <li class="menu-item">
                        <a href="${pageContext.request.contextPath}/WebStatic/info-user.jsp" id="menuProfile">Hồ Sơ</a>
                    </li>

                    <li class="menu-item active">
                        <a href="#" id="menuBank">Ngân Hàng</a>
                    </li>

                    <li class="menu-item">
                        <a href="${pageContext.request.contextPath}/views/user/addresses.jsp" id="menuAddress">Địa Chỉ</a>
                    </li>

                    <li class="menu-item">
                        <a href="${pageContext.request.contextPath}/WebStatic/newPassword.jsp" id="menuPassword">Đổi Mật Khẩu</a>
                    </li>
                </ul>

            </li>

            <!-- ĐƠN MUA -->
            <li class="menu-category">
                <a href="${pageContext.request.contextPath}/WebStatic/order.jsp" id="menuOrders">
                    <i class="fa-solid fa-bag-shopping"></i> Đơn Mua
                </a>
            </li>

            <!-- VOUCHER -->
            <li class="menu-category">
                <a href="${pageContext.request.contextPath}/WebStatic/voucherDetail.jsp" id="menuVoucher">
                    <i class="fa-solid fa-ticket"></i> Kho Voucher
                </a>
            </li>

        </ul>


</div>

</body>
</html>
