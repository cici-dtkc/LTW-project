<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý sản phẩm - Admin</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/vouchersAdmin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/productAdmin.css">
</head>

<body>
<div class="app">

    <!-- SIDEBAR -->
    <%@ include file="/views/includes/sideBarAdmin.jsp" %>

    <!-- MAIN CONTENT -->
    <div class="container">

        <div class="topbar">
            <div>
                <h2>Sản phẩm</h2>
                <div class="muted">Quản lý sản phẩm</div>
            </div>
            <div class="muted">Xin chào, Admin</div>
        </div>

        <!-- TOOLBAR -->
        <div class="toolbar">
            <form method="get" action="${pageContext.request.contextPath}/admin/products">
                <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm...">

                <select name="status">
                    <option value="">Tất cả trạng thái</option>
                    <option value="1">Đang hiển thị</option>
                    <option value="0">Đang ẩn</option>
                </select>

                <select name="category">
                    <option value="">Tất cả danh mục</option>
                    <option value="PHONE">Điện thoại</option>
                    <option value="PART">Linh kiện</option>
                </select>

                <button class="btn-filter">Tìm kiếm</button>
            </form>

            <a href="${pageContext.request.contextPath}/admin/products/add">
                <button class="btn-add">+ Thêm sản phẩm</button>
            </a>
        </div>

        <!-- TABLE -->
        <table class="table-view">
            <thead>
            <tr>
                <th>Hình ảnh</th>
                <th>Tên</th>
                <th>Giá</th>
                <th>Danh mục</th>
                <th>Phiên bản</th>
                <th>Tồn kho</th>
                <th>Hành động</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${products}" var="p">
                <tr>
                    <td>
                        <img src="${pageContext.request.contextPath}/assert/img/product/${p.image}">
                    </td>
                    <td>${p.name}</td>
                    <td>${p.price}₫</td>
                    <td>${p.categoryName}</td>
                    <td>${p.version}</td>
                    <td>${p.stock}</td>
                    <td class="actionsProduct">

                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.id}">
                            <button class="btn-edit">
                                <i class="fa-solid fa-pencil"></i>
                            </button>
                        </a>

                        <button class="btn-delete"
                                onclick="toggleVisibility(${p.id})">
                            <i class="fa-solid ${p.active ? 'fa-eye' : 'fa-eye-slash'}"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div class="footer">
            Hiển thị ${fn:length(products)} sản phẩm
        </div>

    </div>
</div>

<script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
<script src="${pageContext.request.contextPath}/js/productAdmin.js"></script>
</body>
</html>
