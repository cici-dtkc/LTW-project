<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) request.getAttribute("user");
    // Set activeMenu để highlight menu item trong sidebar
    request.setAttribute("activeMenu", "profile");
%>
<html>
<head>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
</head>
<body>
<div id="pageWrapper">
    <jsp:include page="/views/includes/sidebarUser.jsp"/>
    <div class="info-user" id="info-user">
        <div class="container">
            <div class="title">
                <span>Hồ Sơ Của Tôi</span>
            </div>

            <div class="profile">
                <!-- Cột trái -->
                <div class="left">
                    <div class="row">
                        <label class="label">Tên đăng nhập</label>
                        <div class="readonly" id="username-view"><%= user != null ? user.getUsername() : "Người dùng" %></div>
                    </div>

                    <div class="row">
                        <label class="label" for="firstName">Họ</label>
                        <input type="text" id="firstName" class="input" value="<%= user != null && user.getFirstName() != null ? user.getFirstName() : "Người" %>" disabled>
                    </div>
                    <div class="row">
                        <label class="label" for="lastName">Tên</label>
                        <input type="text" id="lastName" class="input" value="<%= user != null && user.getLastName() != null ? user.getLastName() : "dùng" %>" disabled>
                    </div>
                    <div class="row">
                        <label class="label" for="emailInput">Email</label>
                        <input type="email" id="emailInput" class="input" value="<%= user != null && user.getEmail() != null ? user.getEmail() : "nguoidung@gmail.com" %>" disabled>
                        <span class="error" id="emailError"></span>
                    </div>

                    <div class="buttons">
                        <button type="button" class="btn primary" id="btn-edit">Chỉnh sửa</button>
                        <button type="button" class="btn outline" id="btn-logout">Đăng xuất</button>
                        <button type="button" class="btn primary hidden" id="btn-save">Lưu thay đổi</button>
                    </div>
                </div>

                <!-- Cột phải -->
                <div class="right">
                    <div class="avatar">
                        <img id="user-avatar" src="<%= user != null && user.getAvatar() != null ? user.getAvatar() : "" %>" alt="avatar">
                    </div>
                    <button for="avatarInput" class="btn small outline">Chọn Ảnh</button>
                    <input id="avatarInput" type="file" accept="image/png, image/jpeg" class="hidden-input">
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
