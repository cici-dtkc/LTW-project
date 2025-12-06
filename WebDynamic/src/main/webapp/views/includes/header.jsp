<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Website bán điện thoại và phụ kiện di động">
    <meta name="keywords" content="điện thoại, phụ kiện, mobile, smartphone">
    <title>Cửa hàng điện thoại</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WebStatic/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WebStatic/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WebStatic/assert/css/header.css">
</head>
<body>
<!-- Header -->
<header id="header">
    <div class="container">
        <div class="inner-wrap">
            <div class="logo" id="logo">
                <a href="${pageContext.request.contextPath}/WebStatic/home.jsp">
                    <img src="${pageContext.request.contextPath}/WebStatic/assert/img/logo.png" alt="Logo Website" id="logo-img">
                </a>
            </div>

            <nav class="menu" id="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/WebStatic/home.jsp" id="nav-home">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/WebStatic/listproduct.jsp" id="nav-phone">Điện thoại</a></li>
                    <li class="has-mega" id="nav-accessory-item">
                        <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp" id="nav-accessory">Phụ kiện</a>
                        <div class="mega-menu" id="mega-accessory">
                            <div class="mega-inner">
                                <div class="mega-col">
                                    <h4>Linh kiện di động</h4>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Màn hình cảm ứng</a>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Pin</a>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Camera</a>
                                </div>
                                <div class="mega-col">
                                    <h4>&nbsp;</h4>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Loa/mic</a>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Cáp sạc / Cổng sạc</a>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Giá đỡ điện thoại</a>
                                </div>
                                <div class="mega-col">
                                    <h4>&nbsp;</h4>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Quạt tản nhiệt / Cooling fan</a>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Ốp lưng / Vỏ lưng</a>
                                    <a href="${pageContext.request.contextPath}/WebStatic/listproduct_accessory.jsp">Kính cường lực / Mặt kính</a>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/WebStatic/home.jsp#footer" id="nav-contact">Liên hệ</a></li>
                </ul>
            </nav>

            <div class="box" id="icon-box">
                <ul>
                    <li class="search-item">
                        <a href="#" id="btn-search"><i class="fa-solid fa-magnifying-glass"></i></a>
                        <input id="header-search" class="search-input" type="text" placeholder="Tìm kiếm sản phẩm..." />
                    </li>

                    <li class="cart-item">
                        <a href="${pageContext.request.contextPath}/WebStatic/cart.jsp" id="btn-cart">
                            <i class="fa-solid fa-cart-shopping"></i>
                            <span class="cart-badge" id="cart-badge">
                                <c:out value="${sessionScope.cartItemCount != null ? sessionScope.cartItemCount : 0}" />
                            </span>
                        </a>
                    </li>

                    <li id="user-area" class="user-area">
                        <div class="user-profile" id="user-profile">
                            <i class="fa-solid fa-user"></i>
                            <span class="username" id="header-username">
                                <c:choose>
                                    <c:when test="${sessionScope.user != null}">
                                        ${sessionScope.user.username}
                                    </c:when>
                                    <c:otherwise>
                                        Đăng nhập
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="user-dropdown" id="user-dropdown">
                            <a href="${pageContext.request.contextPath}/WebStatic/info-user.jsp">Tài khoản của tôi</a>
                            <a href="${pageContext.request.contextPath}/WebStatic/order.jsp">Đơn mua</a>
                            <c:if test="${sessionScope.user != null}">
                                <a href="${pageContext.request.contextPath}/logout.jsp" id="logout-link">Đăng xuất</a>
                            </c:if>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</header>

<script src="${pageContext.request.contextPath}/WebStatic/js/header.js"></script>
</body>
</html>
