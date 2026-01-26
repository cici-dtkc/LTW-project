<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cửa hàng điện thoại</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/cart.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/listproduct.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/productDetail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/listVouchers.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/home.css">
</head>
<body>
<jsp:include page="/views/includes/header.jsp"/>

<div id="main-content">
    <!-- Slider Banner -->
    <section id="banner-slider" class="slider-banner">
        <div id="slider-container" class="slider">
            <!-- Slide items - Load từ database -->
            <c:forEach var="product" items="${bannerProducts}" varStatus="status">
                <div class="slide ${status.first ? 'active' : ''}" id="slide-${status.index + 1}">
                    <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}">
                        <img src="${pageContext.request.contextPath}/assert/img/product/${product.main_image}" alt="${product.name}">
                    </a>
                </div>
            </c:forEach>

            <!-- Navigation buttons -->
            <button id="btn-prev" class="prev">&#10094;</button>
            <button id="btn-next" class="next">&#10095;</button>
        </div>
    </section>
    <!--    End slider banner-->

    <section class="product-related" id="product-phones">
        <h2 class="title-desc"> Điện thoại mới ra mắt</h2>
        <div id="product-list-phones" class="product-list">
            <c:forEach var="product" items="${featuredProducts}" varStatus="status">
                <c:if test="${status.index < 4}">
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}">
                            <div class="product-img">
                                <img src="${pageContext.request.contextPath}/assert/img/product/${product.main_image}" alt="${product.name}">
                                <c:if test="${product.discount_percentage > 0}">
                                    <span class="discount-badge">-${product.discount_percentage}%</span>
                                </c:if>
                            </div>
                        </a>
                        <div class="product-info">
                            <h2>${product.name}</h2>
                            <div class="price-wrap">
                                    <%-- Giá mới --%>
                                <span class="price-new" id="price-new-${product.id}">
        <fmt:formatNumber value="${product.variants[0].variant_color_price * (1 - product.discount_percentage / 100)}" type="number" groupingUsed="true"/>₫
    </span>

                                    <%-- Giá cũ (Chỉ hiện nếu có giảm giá) --%>
                                <c:if test="${product.discount_percentage > 0}">
        <span class="price-old" id="price-old-${product.id}">
            <fmt:formatNumber value="${product.variants[0].variant_color_price}" type="number" groupingUsed="true"/>₫
        </span>
                                </c:if>
                            </div>
                            <div class="capacity">
                                    <%-- Khởi tạo chuỗi rỗng để chứa các tên đã in --%>
                                <c:set var="printed" value="" />

                                <c:forEach var="variant" items="${product.variants}">
                                    <%-- Trim khoảng trắng để so sánh chính xác tuyệt đối --%>
                                    <c:set var="vName" value="${fn:trim(variant.variant_name)}" />

                                    <%-- Kiểm tra xem tên dung lượng đã tồn tại trong chuỗi 'printed' chưa --%>
                                    <c:if test="${!fn:contains(printed, vName)}">

                                        <button class="${empty printed ? 'active' : ''}"
                                                data-price="${variant.variant_color_price * (1 - product.discount_percentage / 100)}"
                                                data-old-price="${variant.variant_color_price}"
                                                data-id="${variant.variant_color_id}"
                                                data-product-id="${product.id}">
                                                ${variant.variant_name}
                                        </button>

                                        <%-- Lưu vào chuỗi ghi nhớ, dùng dấu phẩy để phân tách các tên --%>
                                        <c:set var="printed" value="${printed},${vName}" />

                                    </c:if>
                                </c:forEach>
                            </div>
                            <div class="rating-cart">
                                <div class="rating">
                                    <i class="fa-solid fa-star"></i>
                                    <i class="fa-solid fa-star"></i>
                                    <i class="fa-solid fa-star"></i>
                                    <i class="fa-solid fa-star"></i>
                                    <i class="fa-regular fa-star"></i>
                                </div>
                            </div>
                            <div class="bottom-info">
                                <span class="sold-count">Đã bán ${product.total_sold}</span>
                                <button class="cart-btn add-to-cart">
                                    <i class="fa-solid fa-cart-plus"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        <div class="view-all-btn">
            <a href="${pageContext.request.contextPath}/listproduct"><i class="fa-solid fa-chevron-right"></i> Xem tất cả</a>
        </div>
    </section>

    <section class="product-related" id="product-accessory">
        <h2 class="title-desc"> Linh kiện mới ra mắt</h2>
        <div id="product-list-accessories" class="product-list">
            <c:forEach var="accessory" items="${featuredAccessories}">
                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/product-detail?id=${accessory.id}">
                        <div class="product-img">
                            <img src="${pageContext.request.contextPath}/assert/img/product/${accessory.main_image}" alt="${accessory.name}">
                            <c:if test="${accessory.discount_percentage > 0}">
                                <span class="discount-badge">-${accessory.discount_percentage}%</span>
                            </c:if>
                        </div>
                    </a>
                    <div class="product-info">
                        <h2>${accessory.name}</h2>
                        <div class="price-wrap">
                            <span class="price-new" id="price-new-${accessory.id}">
        <fmt:formatNumber value="${accessory.variants[0].variant_color_price * (1 - accessory.discount_percentage / 100)}" type="number" groupingUsed="true"/>₫
    </span>
                            <c:if test="${accessory.discount_percentage > 0}">
        <span class="price-old" id="price-old-${accessory.id}">
            <fmt:formatNumber value="${accessory.variants[0].variant_color_price}" type="number" groupingUsed="true"/>₫
        </span>
                            </c:if>
                        </div>
                        <div class="rating-cart">
                            <div class="rating">
                                <i class="fa-solid fa-star"></i>
                                <i class="fa-solid fa-star"></i>
                                <i class="fa-solid fa-star"></i>
                                <i class="fa-solid fa-star"></i>
                                <i class="fa-regular fa-star"></i>
                            </div>
                        </div>
                        <div class="bottom-info">
                            <span class="sold-count">Đã bán ${accessory.total_sold}</span>
                            <button class="cart-btn add-to-cart">
                                <i class="fa-solid fa-cart-plus"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="view-all-btn">
            <a href="${pageContext.request.contextPath}/listproduct"><i class="fa-solid fa-chevron-right"></i> Xem tất cả</a>
        </div>
    </section>
    <!--End section-->

    <!-- Section voucher-->
    <section class="promotions">
        <div class="payment-promo">
            <h3 class="promo-title">ƯU ĐÃI KHUYẾN MÃI</h3>
            <div class="promo-slider">
                <div class="promo-list">
                    <c:forEach var="voucher" items="${activeVouchers}">
                        <div class="promo-item">
                            <i class="fa-solid fa-percent"></i>
                            <div class="promo-content">
                                <p class="discount">
                                    <c:choose>
                                        <c:when test="${voucher.discount_type == 1}">
                                            Giảm ${voucher.discount_value}₫
                                        </c:when>
                                        <c:when test="${voucher.discount_type == 2}">
                                            Giảm ${voucher.discount_value}%
                                        </c:when>
                                        <c:otherwise>
                                            ${voucher.name}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <a href="${pageContext.request.contextPath}/user/voucher-detail?id=${voucher.id}">Xem chi tiết <i class="fa-solid fa-angle-right"></i></a>
                            </div>
                            <div class="promo-status remain">Còn ${voucher.quantity} suất</div>
                        </div>
                    </c:forEach>
                </div>
                <div class="promo-control prev"><i class="fa-solid fa-chevron-left"></i></div>
                <div class="promo-control next"><i class="fa-solid fa-chevron-right"></i></div>
            </div>
        </div>
    </section>
    <!--End section-->

    <!--    Section logo-->
    <section class="brand-section">
        <div class="brand-list">
            <a href="${pageContext.request.contextPath}/listproduct?brandId=1" class="brand-item">
                <img src="${pageContext.request.contextPath}/assert/img/logoIphone.png" alt="Apple">
            </a>
            <a href="${pageContext.request.contextPath}/listproduct?brandId=2" class="brand-item">
                <img src="${pageContext.request.contextPath}/assert/img/logoOppo.png" alt="Oppo">
            </a>
            <a href="${pageContext.request.contextPath}/listproduct?brandId=3" class="brand-item">
                <img src="${pageContext.request.contextPath}/assert/img/logoVivo.png" alt="Vivo">
            </a>
            <a href="${pageContext.request.contextPath}/listproduct?brandId= 4" class="brand-item">
                <img src="${pageContext.request.contextPath}/assert/img/logoSamsung.png" alt="Samsung">
            </a>
        </div>
    </section>
    <!--   End Section logo-->
</div>

<jsp:include page="/views/includes/footer.jsp"/>
<%--<jsp:include page="/views/includes/toast.jsp"/>--%>
<script src="${pageContext.request.contextPath}/js/home.js"></script>
<script src="${pageContext.request.contextPath}/js/header.js"></script>
<script src="${pageContext.request.contextPath}/js/listProduct.js"></script>
<script src="${pageContext.request.contextPath}/js/listVoucher.js"></script>


</body>
</html>