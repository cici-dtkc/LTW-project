<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Khuyến mãi - Admin</title>
<!--    import để lấy icon-->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
            integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"
    />
    <link rel="stylesheet" href="../assert/css/reset.css">
    <link rel="stylesheet" href="../assert/css/base.css">
    <link rel="stylesheet" href="../assert/css/vouchersAdmin.css">
<!--    import để có css sidebar-->
    <link rel="stylesheet" href="../assert/css/sidebarAdmin.css">
</head>
<body>
<div class="app">
<!--    import để chứa sidebar-->
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
    <div class="container">
            <div class="topbar">
                <div class="topbar-left">
                    <div>
                        <h2 class="page-title" id="pageTitle">Khuyến mãi</h2>
                        <div class="muted hidden" id="pageDesc">
                            Quản lý mã khuyến mãi
                        </div>
                    </div>
                </div>
                <div class="topbar-right">
                    <div class="muted hidden">Xin chào, Admin</div>
                </div>
            </div>
        <div class="table-container">
        <div class="toolbar">
            <div class="left">
                <input type="text" id="searchInput" placeholder="Tìm kiếm khuyến mãi...">
                <select id="filterStatus">
                    <option value="all">Tất cả</option>
                    <option value="active">Đang áp dụng</option>
                    <option value="inactive">Hết hạn</option>
                </select>
            </div>
            <button class="btn-add" id="btnOpenModal">+ Thêm Khuyến mãi</button>
        </div>
        <table id="promoTable">
            <thead>
            <tr>
                <th>Mã KM</th>
                <th>Loại KM</th>
                <th>Giảm (%)</th>
                <th>Giảm tối đa (₫)</th>
                <th>Đơn tối thiểu (₫)</th>
                <th>Số lượng</th>
                <th>Ngày bắt đầu</th>
                <th>Ngày kết thúc</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody id="promoBody">
            <tr>
                <td>Giảm 20% toàn bộ sản phẩm</td>
                <td>Phần trăm</td>
                <td>20</td>
                <td>200,000</td>
                <td>500,000</td>
                <td>100</td>
                <td>2025-10-01</td>
                <td>2025-10-31</td>
                <td><span class="status active">Đang áp dụng</span></td>
                <td>
                    <button class="btn-toggle">Tắt</button>
                    <button class="btn-edit">Sửa</button>
                </td>
            </tr>
            <tr>
                <td>Giảm 50k cho đơn từ 300k</td>
                <td>Tiền mặt</td>
                <td>0</td>
                <td>50,000</td>
                <td>300,000</td>
                <td>200</td>
                <td>2025-09-15</td>
                <td>2025-09-30</td>
                <td><span class="status inactive">Hết hạn</span></td>
                <td>
                    <button class="btn-toggle">Bật</button>
                    <button class="btn-edit">Sửa</button>
                </td>
            </tr>
            </tbody>

        </table>
        <!-- Modal Form -->
        <div class="modal" id="promoModal">
            <div class="modal-content">
                <h3>Thêm Khuyến mãi mới</h3>
                <form id="promoForm">
                    <label>Mã KM</label>
                    <input type="text" id="promoCode" required>

                    <label>Loại khuyến mãi</label>
                    <select id="promoType" required>
                        <option value="percent">Giảm theo %</option>
                        <option value="amount">Giảm theo số tiền</option>
                        <option value="gift">Tặng quà</option>
                    </select>

                    <label>Giảm (%)</label>
                    <input type="number" id="promoDiscount" min="0" max="100" required>

                    <label>Giảm tối đa (₫)</label>
                    <input type="number" id="promoMaxDiscount" min="0" required>

                    <label>Đơn tối thiểu (₫)</label>
                    <input type="number" id="promoMinOrder" min="0" required>

                    <label>Số lượng</label>
                    <input type="number" id="promoQuantity" min="1" required>

                    <label>Ngày bắt đầu</label>
                    <input type="date" id="promoStart" required>

                    <label>Ngày kết thúc</label>
                    <input type="date" id="promoEnd" required>

                    <div class="modal-buttons">
                        <button type="submit" class="btn-save">Lưu</button>
                        <button type="button" class="btn-cancel" id="btnCloseModal">Hủy</button>
                    </div>
                </form>
            </div>
        </div>
            <div class="footer" id="footer-pagination">
                <p id="record-info">Hiển thị 2 mã khuyến mãi</p>
                <div class="pagination" id="pagination-number">1</div>
            </div>
        </div>
    </div>
</div>
<!-- Toast Message -->
<div id="toast"></div>
</body>
<!--  import chứa logic sidebar-->
    <script src="../js/sidebarAdmin.js"></script>
    <script src="../js/voucherAdmin.js"></script>
    <script src="../js/dashboardAdmin.js"></script>
</html>
