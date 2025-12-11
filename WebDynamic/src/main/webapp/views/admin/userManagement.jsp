<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/userManagement.css">
    <link rel="stylesheet"
          href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">

    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
</head>
<body>
<div class="app">
<%--    <jsp:include page="/views/includes/sideBarAdmin.jsp"/>--%>
    <%@ include file="/views/includes/sideBarAdmin.jsp" %>
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

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Avatar</th>
                <th>Username</th>
                <th>Role</th>
                <th>Status</th>
                <th>Hành động</th>
            </tr>
            </thead>

            <tbody id="user-table-body">
            <c:forEach var="u" items="${users}">
                <tr data-id="${u.id}">

                    <!-- CỘT ID -->
                    <td class="id-cell">${u.id}</td>

                    <!-- CỘT AVATAR -->
                    <td class="avatar-cell">
                        <img src="${u.avatar}" style="width:40px;height:40px;border-radius:50%;">
                    </td>

                    <!-- USERNAME -->
                    <td>${u.username}</td>

                    <!-- ROLE -->
                    <td class="role-cell">
                <span class="role-text">
                    <c:choose>
                        <c:when test="${u.role == 1}">Admin</c:when>
                        <c:otherwise>User</c:otherwise>
                    </c:choose>
                </span>

                        <select class="role-select" style="display:none;">
                            <option value="0" ${u.role == 0 ? "selected" : ""}>User</option>
                            <option value="1" ${u.role == 1 ? "selected" : ""}>Admin</option>
                        </select>
                    </td>

                    <!-- STATUS -->
                    <td class="status-cell">
                <span class="status-text">
                    <c:choose>
                        <c:when test="${u.status == 1}">Hoạt động</c:when>
                        <c:otherwise>Tạm khóa</c:otherwise>
                    </c:choose>
                </span>

                        <select class="status-select" style="display:none;">
                            <option value="1" ${u.status == 1 ? "selected" : ""}>Hoạt động</option>
                            <option value="0" ${u.status == 0 ? "selected" : ""}>Tạm khóa</option>
                        </select>
                    </td>

                    <!-- ACTION ICONS -->
                    <td>
                        <i class="fa-solid fa-pen edit-icon" style="cursor:pointer;"></i>
                        <i class="fa-solid fa-floppy-disk save-icon" style="display:none;cursor:pointer;"></i>
                        <i class="fa-solid fa-xmark cancel-icon" style="display:none;cursor:pointer;"></i>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>




    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/userManagement.js"></script>
<script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboardAdmin.js"></script>
</html>
