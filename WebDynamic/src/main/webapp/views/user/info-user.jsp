<%--
  Created by IntelliJ IDEA.
  User: mtri2
  Date: 12/5/2025
  Time: 9:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                        <div class="readonly" id="username-view">testuser</div>
                    </div>

                    <div class="row">
                        <label class="label" for="firstName">Họ</label>
                        <input type="text" id="firstName" class="input" value="user" disabled>
                    </div>
                    <div class="row">
                        <label class="label" for="lastName">Tên</label>
                        <input type="text" id="lastName" class="input" value="user" disabled>
                    </div>
                    <div class="row">
                        <label class="label" for="emailInput">Email</label>
                        <input type="email" id="emailInput" class="input" value="testuser@example.com" disabled>
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
                        <img id="user-avatar" src="./assert/img/admin.jpg" alt="avatar">
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
