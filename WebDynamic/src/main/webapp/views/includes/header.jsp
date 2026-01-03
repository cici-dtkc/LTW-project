<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Header -->
<header id="header" data-context-path="${pageContext.request.contextPath}">
    <div class="container">
        <div class="inner-wrap">
            <div class="logo" id="logo">
                <a href="${pageContext.request.contextPath}/home">

                    <img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="Logo Website" id="logo-img">
                </a>
            </div>

            <nav class="menu" id="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home" id="nav-home">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/listproduct" id="nav-phone">Điện thoại</a></li>
                    <li class="has-mega" id="nav-accessory-item">
                        <a href="${pageContext.request.contextPath}/listproduct_accessory" id="nav-accessory">Linh kiện</a>
                        <div class="mega-menu" id="mega-accessory">
                            <div class="mega-inner">
                                <div class="mega-col">
                                    <h4>Linh kiện di động</h4>
                                    <c:url var="manHinhUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Màn hình cảm ứng"/>
                                    </c:url>
                                    <a href="${manHinhUrl}">Màn hình cảm ứng</a>
                                    
                                    <c:url var="pinUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Pin"/>
                                    </c:url>
                                    <a href="${pinUrl}">Pin</a>
                                    
                                    <c:url var="cameraUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Camera"/>
                                    </c:url>
                                    <a href="${cameraUrl}">Camera</a>
                                </div>
                                <div class="mega-col">
                                    <h4>&nbsp;</h4>
                                    <c:url var="loaMicUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Loa/ Mic"/>
                                    </c:url>
                                    <a href="${loaMicUrl}">Loa/mic</a>
                                    
                                    <c:url var="capSacUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Cáp sạc/ Cổng sạc"/>
                                    </c:url>
                                    <a href="${capSacUrl}">Cáp sạc / Cổng sạc</a>
                                    
                                    <c:url var="giaDoUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Giá đỡ điện thoại"/>
                                    </c:url>
                                    <a href="${giaDoUrl}">Giá đỡ điện thoại</a>
                                </div>
                                <div class="mega-col">
                                    <h4>&nbsp;</h4>
                                    <c:url var="quatTanUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Quạt tản nhiệt / Cooling fan"/>
                                    </c:url>
                                    <a href="${quatTanUrl}">Quạt tản nhiệt / Cooling fan</a>
                                    
                                    <c:url var="opLungUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Ốp lưng / Vỏ lưng"/>
                                    </c:url>
                                    <a href="${opLungUrl}">Ốp lưng / Vỏ lưng</a>
                                    
                                    <c:url var="kinhCuongUrl" value="/listproduct_accessory">
                                        <c:param name="type" value="Kính cường lực / Mặt kính"/>
                                    </c:url>
                                    <a href="${kinhCuongUrl}">Kính cường lực / Mặt kính</a>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/home#footer" id="nav-contact">Liên hệ</a></li>
                </ul>
            </nav>

            <div class="box" id="icon-box">
                <ul>
                    <li class="search-item">
                        <a href="#" id="btn-search"><i class="fa-solid fa-magnifying-glass"></i></a>
                        <input id="header-search" class="search-input" type="text" placeholder="Tìm kiếm sản phẩm..." />
                    </li>

                    <li class="cart-item">
                        <a href="${pageContext.request.contextPath}/cart" id="btn-cart">
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
                                    <c:when test="${not empty sessionScope.user}">
                                        ${sessionScope.user.username}
                                    </c:when>
                                    <c:otherwise>
                                        Đăng nhập
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="user-dropdown" id="user-dropdown">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <a href="${pageContext.request.contextPath}/user/profile">Tài khoản của tôi</a>
                                    <a href="${pageContext.request.contextPath}/user/order">Đơn mua</a>
                                    <a href="${pageContext.request.contextPath}/logout" id="logout-link">Đăng xuất</a>
                                </c:when>
                            </c:choose>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</header>
<script src="${pageContext.request.contextPath}/js/header.js"></script>