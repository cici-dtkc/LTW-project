<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Đơn Hàng</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../assert/css/reset.css">
    <link rel="stylesheet" href="../assert/css/base.css">
    <link rel="stylesheet" href="../assert/css/sidebarAdmin.css">
    <link rel="stylesheet" href="../assert/css/admin-orders.css">
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
                    <select id="statusFilter" class="filter-select">
                        <option value="">Tất cả trạng thái</option>
                        <option value="dang-len-don">Đang lên đơn</option>
                        <option value="dang-giao">Đang giao</option>
                        <option value="da-giao">Đã giao</option>
                        <option value="huy">Hủy</option>
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
                    <tr>
                        <td>DH1008</td>
                        <td>Nguyễn Văn C</td>
                        <td>2025-11-10</td>
                        <td>
                            <select class="status-select">
                                <option value="dang-len-don" selected>Đang lên đơn</option>
                                <option value="dang-giao"  >Đang giao</option>
                                <option value="da-giao">Đã giao</option>
                                <option value="huy">Hủy</option>
                            </select>
                        </td>
                        <td>Thanh toán khi nhận hàng</td>
                        <td>₫2,100,000</td>
                    </tr>
                    <tr>
                        <td>DH1007</td>
                        <td>Phạm F</td>
                        <td>2025-18-09</td>
                        <td>
                            <select class="status-select">
                                <option value="dang-len-don">Đang lên đơn</option>
                                <option value="dang-giao"selected >Đang giao</option>
                                <option value="da-giao"  >Đã giao</option>
                                <option value="huy">Hủy</option>
                            </select>
                        </td>
                        <td>Chuyển khoản</td>
                        <td>₫850,000</td>
                    </tr>
                    <tr>
                        <td>DH1006</td>
                        <td>Phạm G</td>
                        <td>2025-12-09</td>
                        <td>
                            <select class="status-select">
                                <option value="dang-len-don" >Đang lên đơn</option>
                                <option value="dang-giao"selected>Đang giao</option>
                                <option value="da-giao"  >Đã giao</option>
                                <option value="huy">Hủy</option>
                            </select>
                        </td>
                        <td>Chuyển khoản</td>
                        <td>₫150,000</td>
                    </tr>
                    <tr>
                        <td>DH1005</td>
                        <td>Phạm X</td>
                        <td>2025-11-09</td>
                        <td>
                            <select class="status-select">
                                <option value="dang-len-don">Đang lên đơn</option>
                                <option value="dang-giao" selected>Đang giao</option>
                                <option value="da-giao"  >Đã giao</option>
                                <option value="huy">Hủy</option>
                            </select>
                        </td>
                        <td>Chuyển khoản</td>
                        <td>₫950,000</td>
                    </tr>
                    <tr>
                        <td>DH1004</td>
                        <td>Phạm D</td>
                        <td>2025-11-09</td>
                        <td>
                            <select class="status-select">
                                <option value="dang-len-don">Đang lên đơn</option>
                                <option value="dang-giao">Đang giao</option>
                                <option value="da-giao" selected>Đã giao</option>
                                <option value="huy">Hủy</option>
                            </select>
                        </td>
                        <td>Thanh toán khi nhận hàng</td>
                        <td>₫850,000</td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </main>
</div>
<script src="../js/sidebarAdmin.js"></script>
</body>
</html>
