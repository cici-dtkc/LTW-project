<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/vouchersAdmin.css">
    <!--    import để có css sidebar-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
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
                    <a class="nav-link" data-target="dashboard" href="${pageContext.request.contextPath}/admin/dashboardAdmin.jsp"
                    ><span class="icon"><i class="fa-solid fa-chart-pie"></i></span>
                        <span class="nav-label">Tổng Quan</span></a
                    >
                    <span class="nav-tooltip">Tổng Quan</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="products" href="${pageContext.request.contextPath}/admin/productAdmin.jsp"
                    ><span class="icon"><i class="fa-solid fa-box-open"></i></span>
                        <span class="nav-label">Sản phẩm</span></a
                    >
                    <span class="nav-tooltip">Sản phẩm</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="orders" href="${pageContext.request.contextPath}/admin/admin-orders.jsp"
                    ><span class="icon"><i class="fa-solid fa-receipt"></i></span> <span class="nav-label">Đơn
              hàng</span></a
                    >
                    <span class="nav-tooltip">Đơn hàng</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="customers" href="${pageContext.request.contextPath}/admin/userManagement.jsp"
                    ><span class="icon"><i class="fa-solid fa-users"></i></span> <span class="nav-label">Người dùng</span></a
                    >
                    <span class="nav-tooltip">Người dùng</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-target="promotions" href="${pageContext.request.contextPath}/admin/vouchers"
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
                    <form method="get" action="${pageContext.request.contextPath}/admin/vouchers" style="display: inline-flex; gap: 10px;">
                        <input type="text" name="keyword" id="searchInput" placeholder="Tìm kiếm khuyến mãi..." value="${keyword}">
                        <select name="status" id="filterStatus" onchange="this.form.submit()">
                            <option value="-1" ${status == -1 ? 'selected' : ''}>Tất cả</option>
                            <option value="1" ${status == 1 ? 'selected' : ''}>Đang áp dụng</option>
                            <option value="0" ${status == 0 ? 'selected' : ''}>Hết hạn</option>
                        </select>
                        <button type="submit" style="display: none;">Tìm</button>
                    </form>
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
                <c:choose>
                    <c:when test="${not empty vouchers}">
                        <c:forEach var="voucher" items="${vouchers}">
                            <tr>
                                <td>${voucher.voucherCode}</td>
                                <td>${voucher.type == 1 ? 'Phần trăm' : (voucher.type == 2 ? 'Tiền mặt' : 'Tặng quà')}</td>
                                <td>${voucher.discountAmount}${voucher.type == 1 ? '%' : ''}</td>
                                <td><fmt:formatNumber value="${voucher.maxReduce}" pattern="#,###" />₫</td>
                                <td><fmt:formatNumber value="${voucher.minOrderValue}" pattern="#,###" />₫</td>
                                <td>${voucher.quantity}</td>
                                <td>${voucher.startDate != null ? voucher.startDate.toString().substring(0, 10) : ''}</td>
                                <td>${voucher.endDate != null ? voucher.endDate.toString().substring(0, 10) : ''}</td>
                                <td>
                                <span class="status ${voucher.status == 1 ? 'active' : 'inactive'}">
                                        ${voucher.status == 1 ? 'Đang áp dụng' : 'Hết hạn'}
                                </span>
                                </td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/vouchers" style="display: inline;">
                                        <input type="hidden" name="action" value="toggleStatus">
                                        <input type="hidden" name="id" value="${voucher.id}">
                                        <input type="hidden" name="status" value="${voucher.status}">
                                        <button type="submit" class="btn-toggle">${voucher.status == 1 ? 'Tắt' : 'Bật'}</button>
                                    </form>
                                    <button class="btn-edit" onclick="editVoucher('${voucher.id}')">Sửa</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" style="text-align: center; padding: 20px;">
                                Không có dữ liệu khuyến mãi
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
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
                <p id="record-info">Hiển thị ${empty vouchers ? 0 : vouchers.size()} mã khuyến mãi (Tổng: ${totalVoucher})</p>
                <div class="pagination" id="pagination-number">
                    <c:if test="${page > 1}">
                        <a href="${pageContext.request.contextPath}/admin/vouchers?page=${page - 1}&keyword=${keyword}&status=${status}">‹</a>
                    </c:if>
                    <c:forEach begin="1" end="${totalPage}" var="i">
                        <c:choose>
                            <c:when test="${i == page}">
                                <span class="active">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/admin/vouchers?page=${i}&keyword=${keyword}&status=${status}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${page < totalPage}">
                        <a href="${pageContext.request.contextPath}/admin/vouchers?page=${page + 1}&keyword=${keyword}&status=${status}">›</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Toast Message -->
<div id="toast"></div>
</body>
<!--  import chứa logic sidebar-->
<script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
<script src="${pageContext.request.contextPath}/js/voucherAdmin.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboardAdmin.js"></script>
</html>
