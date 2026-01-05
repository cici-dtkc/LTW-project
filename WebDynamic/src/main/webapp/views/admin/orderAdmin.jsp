<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="isAjax" value="${param.ajax == 'true'}"/>

<c:if test="${!isAjax}">
    <!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Đơn Hàng</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"/>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/admin-orders.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
    </head>
    <body>

    <script>
        const contextPath = '${pageContext.request.contextPath}';
    </script>

    <div class="app">
    <%@ include file="/views/includes/sideBarAdmin.jsp" %>

    <main class="content">
    <div class="orders-section">

    <h2>Quản Lý Đơn Hàng</h2>

    <div class="search-filter-bar">
        <div class="search-box">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="text" id="searchInput"
                   placeholder="Tìm theo mã đơn hoặc tên khách hàng">
        </div>

        <div class="filter-options">
            <select id="statusFilter" class="filter-select">
                <option value="">Tất cả trạng thái</option>
                <option value="1">Đang lên đơn</option>
                <option value="2">Đang giao</option>
                <option value="3">Đã giao</option>
                <option value="5">Hủy</option>
            </select>
        </div>
    </div>


    <table class="orders-table" id="ordersTable">
    <thead>
    <tr>
        <th>Mã ĐH</th>
        <th>Khách</th>
        <th>Ngày đặt</th>
        <th>Trạng thái</th>
        <th>Thanh toán</th>
        <th>Tổng</th>
    </tr>
    </thead>
    <tbody>
</c:if>

<!-- ================= TBODY (AJAX + NORMAL) ================= -->
<c:forEach var="o" items="${orders}">
    <tr>
        <td>DH${o.id}</td>
        <td>${o.userId}</td>
        <td>${o.createdAt}</td>

        <td>
            <select class="status-select status-${o.status}" data-id="${o.id}">
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

<c:if test="${!isAjax}">
    </tbody>
    </table>

    </div>
    </main>
    </div>
    <div id="toast" class="toast"></div>

    <script src="${pageContext.request.contextPath}/js/orderAdmin.js"></script>
    <script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
    </body>
    </html>
</c:if>
