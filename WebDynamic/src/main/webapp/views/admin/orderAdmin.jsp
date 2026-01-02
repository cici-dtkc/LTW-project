<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Đơn Hàng</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/admin-orders.css">
</head>

<body>
<div class="app">
    <aside class="sidebar" id="sidebar">
        <div class="brand">
            <button
                    class="toggle-icon sidebar-toggle"
                    id="sidebarToggle"
                    style="cursor: pointer"
            >
                <i class="fa-solid fa-bars" style="font-size: 16px"></i>
            </button>
            <div>
                <div class="muted">Quản lý bán hàng</div>
            </div>
        </div>

        <div class="section-title">Điều hướng</div>
        <nav class="nav-section" id="nav-section">
            <ul class="nav-list primary-nav" id="nav">
                <li class="nav-item">
                    <a class="nav-link" data-target="dashboard" href="dashboardAdmin.html"
                    ><span class="icon"><i class="fa-solid fa-chart-pie"></i></span>
                        <span class="nav-label">Tổng Quan</span></a
                    >
                    <span class="nav-tooltip">Tổng Quan</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="products" href="productAdmin.html"
                    ><span class="icon"><i class="fa-solid fa-box-open"></i></span>
                        <span class="nav-label">Sản phẩm</span></a
                    >
                    <span class="nav-tooltip">Sản phẩm</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="orders" href="admin-orders.html"
                    ><span class="icon"><i class="fa-solid fa-receipt"></i></span> <span class="nav-label">Đơn
              hàng</span></a
                    >
                    <span class="nav-tooltip">Đơn hàng</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="customers" href="userManagement.html"
                    ><span class="icon"><i class="fa-solid fa-users"></i></span> <span class="nav-label">Người dùng</span></a
                    >
                    <span class="nav-tooltip">Người dùng</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="promotions" href="vouchersAdmin.html"
                    ><span class="icon"><i class="fa-solid fa-tags"></i></span> <span class="nav-label">Khuyến
              mãi</span></a
                    >
                    <span class="nav-tooltip">Khuyến mãi</span>
                </li>
            </ul>
            <ul class="nav-list second-nav">
                <li class="nav-item">
                    <a class="nav-link" data-target="logout" href="#">
                    <span class="icon">
                    <i class="fa-solid fa-right-from-bracket"></i></span>
                        <span class="nav-label">Đăng xuất</span>
                    </a>
                    <span class="nav-tooltip">Đăng xuất</span>
                </li>
            </ul>
        </nav>
    </aside>
    <!-- MAIN CONTENT -->
    <main class="content">
        <div class="orders-section">
            <h2>Quản Lý Đơn Hàng</h2>

            <!-- Search and Filter Section -->
            <div class="search-filter-bar">
                <div class="search-box">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input
                            type="text"
                            id="searchInput"
                            placeholder="Tìm kiếm theo mã đơn hàng hoặc tên khách hàng..."
                            autocomplete="off"
                    />
                    <button type="button" id="clearSearch" class="clear-btn" style="display: none;">
                        <i class="fa-solid fa-times"></i>
                    </button>
                </div>
                <div class="filter-options">
                    <select id="statusFilter">
                        <option value="">Tất cả</option>
                        <option value="1">Đang lên đơn</option>
                        <option value="2">Đang giao</option>
                        <option value="3">Đã giao</option>
                        <option value="5">Hủy</option>
                    </select>
                </div>
            </div>

            <div class="table-container">
                <table class="orders-table" id="ordersTable">
                    <thead>
                    <tr>
                        <th>Mã ĐH</th>
                        <th>Khách</th>
                        <th>Ngày đặt</th>
                        <th>Trạng thái</th>
                        <th>Phương thức thanh toán</th>
                        <th>Tổng</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="o" items="${orders}">
                        <tr>
                            <td>DH${o.id}</td>
                            <td>${o.userId}</td>
                            <td>${o.createdAt}</td>

                            <td>
                                <select class="status-select" data-id="${o.id}">
                                    <option value="1" ${o.status == 1 ? 'selected' : ''}>Đang lên đơn</option>
                                    <option value="2" ${o.status == 2 ? 'selected' : ''}>Đang giao</option>
                                    <option value="3" ${o.status == 3 ? 'selected' : ''}>Đã giao</option>
                                    <option value="5" ${o.status == 5 ? 'selected' : ''}>Hủy</option>
                                </select>
                            </td>

                            <td>COD</td>
                            <td>₫${o.totalAmount}</td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>

            </div>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
<script src="${pageContext.request.contextPath}/js/orderAdmin.js"></script>
</body>
</html>
