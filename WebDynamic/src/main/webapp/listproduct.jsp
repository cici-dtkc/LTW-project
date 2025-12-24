<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Danh Sách Sản Phẩm - Điện Thoại</title>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/listproduct.css">
</head>

<body>
<jsp:include page="/views/includes/header.jsp"/>

<main class="product-page">
    <!-- Thanh lọc & sắp xếp -->
    <div class="product-filter-bar">
        <!-- Hàng 1: Bộ lọc + thương hiệu -->
        <div class="filter-top">
            <div class="filter-btn">
                <i class="fa-solid fa-filter"></i>
                <span>Bộ lọc</span>
            </div>

            <div class="brand-list">
                <div class="brand"><img src="assert/img/logoSamsung.png" alt="Samsung"></div>
                <div class="brand"><img src="assert/img/logoIphone.png" alt="iPhone"></div>
                <div class="brand"><img src="assert/img/logoOppo.png" alt="Oppo"></div>
                <div class="brand"><img src="assert/img/logoVivo.png" alt="Vivo"></div>
            </div>
        </div>

        <!-- Hàng 2: Các nút lọc -->
        <div class="filter-options">
            <div class="filter-item" data-filter="gia">Giá </div>
            <div class="filter-item" data-filter="bonho">Bộ nhớ </div>
            <div class="filter-item" data-filter="mausac">Màu sắc </div>
            <div class="filter-item" data-filter="namramat">Năm ra mắt </div>
        </div>

        <!-- Menu phụ cho từng bộ lọc -->
        <div class="dropdown" id="gia">
            <h4 style="margin-top: 0">Hãy chọn mức giá phù hợp với bạn</h4>
            <div class="price-input">
                <input type="number" placeholder="Từ">
                <span>-</span>
                <input type="number" placeholder="Đến">
            </div>
            <div class="dropdown-actions">
                <button class="btn-close">Đóng</button>
                <button class="btn-apply">Xem kết quả</button>
            </div>
        </div>

        <div class="dropdown" id="bonho">
            <div class="option-group">
                <button>64GB</button>
                <button>128GB</button>
                <button>256GB</button>
                <button>512GB</button>
            </div>
            <div class="dropdown-actions">
                <button class="btn-close">Đóng</button>
                <button class="btn-apply">Xem kết quả</button>
            </div>
        </div>

        <div class="dropdown" id="mausac">
            <div class="option-group">
                <button>Đen</button>
                <button>Trắng</button>
                <button>Xanh</button>
                <button>Hồng</button>
                <button>Bạc</button>
            </div>
            <div class="dropdown-actions">
                <button class="btn-close">Đóng</button>
                <button class="btn-apply">Xem kết quả</button>
            </div>
        </div>

        <div class="dropdown" id="namramat">
            <div class="option-group">
                <button>2021</button>
                <button>2022</button>
                <button>2023</button>
                <button>2024</button>
                <button>2025</button>
            </div>
            <div class="dropdown-actions">
                <button class="btn-close">Đóng</button>
                <button class="btn-apply">Xem kết quả</button>
            </div>
        </div>
    </div>

    <!-- Phần sắp xếp -->
    <div class="sort-section">
        <span>Sắp xếp theo:</span>
        <ul id="sortList">
            <li class="active">Nổi bật</li>
            <li>Bán chạy</li>
            <li>Giảm giá</li>
            <li>Mới</li>
            <li>Giá <i class="arrow"></i></li>
        </ul>
    </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div id="product-list" class="product-list phone">
        <c:forEach var="product" items="${products}">
            <div class="product-card">
                <a href="productDetail.jsp?id=${product.id}">
                    <div class="product-img">
                        <img src="${product.image}" alt="${product.name}">
                        <c:if test="${product.discount > 0}">
                            <span class="discount-badge">-${product.discount}%</span>
                        </c:if>
                    </div>
                </a>
                <div class="product-info">
                    <h2>${product.name}</h2>
                    <div class="price-wrap">
            <span class="price-new">
              <fmt:formatNumber value="${product.priceNew}" type="number" groupingUsed="true"/>₫
            </span>
                        <c:if test="${product.priceOld > product.priceNew}">
              <span class="price-old">
                <fmt:formatNumber value="${product.priceOld}" type="number" groupingUsed="true"/>₫
              </span>
                        </c:if>
                    </div>
                    <div class="capacity">
                        <c:forEach var="variant" items="${product.variants}" varStatus="status">
                            <button class="${status.first ? 'active' : ''}"
                                    data-price="${variant.priceNew}"
                                    data-old-price="${variant.priceOld}">
                                    ${variant.name}
                            </button>
                        </c:forEach>
                    </div>
                    <div class="rating-cart">
                        <div class="rating">
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= product.rating}">
                                        <i class="fa-solid fa-star"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa-regular fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="bottom-info">
                        <span class="sold-count">Đã bán ${product.soldCount}</span>
                        <button class="cart-btn">
                            <i class="fa-solid fa-cart-plus"></i>
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Thông báo khi không có sản phẩm -->
    <c:if test="${empty products}">
        <div style="text-align: center; padding: 50px;">
            <p style="font-size: 18px; color: #999;">Không có sản phẩm nào</p>
        </div>
    </c:if>

    <!-- nút xem thêm -->
    <div class="load-more-wrap">
        <button id="loadMoreBtn" class="btn">Xem thêm</button>
        <span id="loadMoreSpinner" class="spinner" style="display:none">Đang tải...</span>
    </div>

</main>
<jsp:include page="/views/includes/footer.jsp"/>

<script src="${pageContext.request.contextPath}/js/listProduct.js"></script>
<script src="${pageContext.request.contextPath}/js/header.js"></script>
</body>
</html>