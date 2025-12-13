<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) request.getAttribute("user");
    request.setAttribute("activeMenu", "profile");
    String avatarPath;
    if (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty()) {
        String avatar = user.getAvatar();

        if (avatar.startsWith("/")) {
            avatarPath = request.getContextPath() + avatar;
        } else {
            avatarPath = request.getContextPath() + "/" + avatar;
        }
    } else {
        avatarPath = request.getContextPath() + "/assert/img/admin.jpg";
    }
%>
<html>
<head>
    <title>Hồ Sơ Người Dùng</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/toast.css">
</head>

<body>
<div id="pageWrapper">

    <jsp:include page="/views/includes/sidebarUser.jsp"/>

    <div class="info-user">
        <div class="container">

            <div class="title">
                <span>Hồ Sơ Của Tôi</span>
            </div>

            <div class="profile">

                <%-- FORM 1: UPDATE PROFILE --%>
                <form id="update-form" class="left"
                      method="POST"
                      action="${pageContext.request.contextPath}/user/profile">

                    <input type="hidden" name="action" value="update">

                    <div class="row">
                        <label class="label">Tên đăng nhập</label>
                        <div class="readonly"><%= user != null ? user.getUsername() : "" %>
                        </div>
                    </div>

                    <div class="row">
                        <label for="firstName" class="label">Họ</label>
                        <input type="text" id="firstName" name="firstName" class="input"
                               value="<%= user != null && user.getFirstName() != null ? user.getFirstName() : "" %>"
                               disabled>
                    </div>

                    <div class="row">
                        <label for="lastName" class="label">Tên</label>
                        <input type="text" id="lastName" name="lastName" class="input"
                               value="<%= user != null && user.getLastName() != null ? user.getLastName() : "" %>"
                               disabled>
                    </div>

                    <div class="row">
                        <label for="emailInput" class="label">Email</label>
                        <input type="email" id="emailInput" name="email" class="input"
                               value="<%= user != null && user.getEmail() != null ? user.getEmail() : "" %>"
                               disabled>
                        <span class="error" id="emailError"></span>
                    </div>

                    <div class="buttons">
                        <button type="button" class="btn primary" id="btn-edit">Chỉnh sửa</button>
                        <button type="button" class="btn outline" id="btn-logout">Đăng xuất</button>
                        <button type="submit" class="btn primary hidden" id="btn-save">Lưu thay đổi</button>
                    </div>
                </form>

                <%-- FORM 2: UPLOAD AVATAR --%>
                <form id="avatar-form" class="right"
                      method="POST"
                      action="${pageContext.request.contextPath}/user/profile"
                      enctype="multipart/form-data">

                    <input type="hidden" name="action" value="upload-avatar">

                    <div class="avatar">
                        <img id="user-avatar"
                             src="<%= avatarPath %>"
                             alt="User Avatar"
                             class="avatar-img"
                             onerror="this.src='${pageContext.request.contextPath}/assert/img/admin.jpg'">
                    </div>

                    <label for="avatarInput" class="btn small outline">Chọn Ảnh</label>

                    <input id="avatarInput"
                           name="avatar"
                           type="file"
                           accept="image/png, image/jpeg"
                           class="hidden-input">
                </form>

            </div>
        </div>
    </div>
</div>

</body>
<script src="${pageContext.request.contextPath}/js/info-user.js"></script>
</html>
