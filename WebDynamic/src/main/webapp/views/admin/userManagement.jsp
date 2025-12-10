<%--
  Created by IntelliJ IDEA.
  User: mtri2
  Date: 12/8/2025
  Time: 10:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/userManagement.css">
</head>
<body>
<div class="container" id="container-user-management">

    <div class="topbar">
        <div class="topbar-left">
            <div>
                <h2 class="page-title" id="title-user-management">Quản lý người dùng</h2>
            </div>
        </div>
    </div>

    <div class="header" id="filter-bar">
        <input type="text" placeholder="Tìm kiếm..." id="search-input">
        <select id="role-filter">
            <option value="">Vai trò</option>
            <option value="Admin">Admin</option>
            <option value="User">User</option>
        </select>
        <select id="status-filter">
            <option value="">Trạng thái</option>
            <option value="Hoạt động">Hoạt động</option>
            <option value="Tạm khóa">Tạm khóa</option>
        </select>
    </div>

    <table id="user-table">
        <thead id="user-table-head">
        <tr>
            <th>ID</th>
            <th>Ảnh đại diện</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
        </tr>
        </thead>
        <tbody id="user-table-body">
        <!-- Dữ liệu người dùng sẽ được thêm từ database tại đây -->
        <tr data-id="1">
            <td>1</td>
            <td><img src="../assert/img/admin.jpg" style="width:32px; height:32px; border-radius:50%;"></td>
            <td>Đỗ Thị</td>
            <td>Tri@example.com</td>
            <td class="role-cell">User</td>
            <td class="status-cell">Hoạt động</td>
            <td>
                <i class="fa-solid fa-pen edit-icon" style="cursor:pointer;"></i>
                <i class="fa-solid fa-floppy-disk save-icon" style="cursor:pointer; display:none;"></i>
                <i class="fa-solid fa-xmark cancel-icon" style="cursor:pointer; display:none;"></i>
            </td>
        </tr>
        <tr data-id="2">
            <td>2</td>
            <td><img src="../assert/img/admin.jpg" style="width:32px; height:32px; border-radius:50%;"></td>
            <td>Hé Mầm</td>
            <td>mam@example.com</td>
            <td class="role-cell">Admin</td>
            <td class="status-cell">Tạm khóa</td>
            <td>
                <i class="fa-solid fa-pen edit-icon" style="cursor:pointer;"></i>
                <i class="fa-solid fa-floppy-disk save-icon" style="cursor:pointer; display:none;"></i>
                <i class="fa-solid fa-xmark cancel-icon" style="cursor:pointer; display:none;"></i>
            </td>
        </tr>
        <tr data-id="3">
            <td>3</td>
            <td><img src="../assert/img/admin.jpg" style="width:32px; height:32px; border-radius:50%;"></td>
            <td>Da Vân</td>
            <td>van@example.com</td>
            <td class="role-cell">User</td>
            <td class="status-cell">Hoạt động</td>
            <td>
                <i class="fa-solid fa-pen edit-icon" style="cursor:pointer;"></i>
                <i class="fa-solid fa-floppy-disk save-icon" style="cursor:pointer; display:none;"></i>
                <i class="fa-solid fa-xmark cancel-icon" style="cursor:pointer; display:none;"></i>
            </td>
        </tr>
        </tbody>
    </table>

</div>
</body>
<script>window.appContext='${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/js/userManagement.js"></script>
<script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
</html>
