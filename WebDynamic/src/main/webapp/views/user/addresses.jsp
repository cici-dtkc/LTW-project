<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    User user = (User) request.getAttribute("user");
    request.setAttribute("activeMenu", "address");
%>

<html>
<head>
    <title>Địa chỉ của tôi</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/addresses.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
</head>

<body>
<jsp:include page="/views/includes/header.jsp"/>
<div id="pageWrapper">

    <jsp:include page="/views/includes/sidebarUser.jsp"/>

    <div class="address-content">
        <div class="address-header">
            <h1 class="address-title">Địa chỉ của tôi</h1>
            <button class="btn-add-address" id="btnAddAddress">
                <i class="fa-solid fa-plus"></i> Thêm địa chỉ mới
            </button>
        </div>

        <div class="address-section">
            <h2 class="section-title">Địa chỉ</h2>

            <div id="addressList" class="address-list">

                <!-- CÓ ĐỊA CHỈ -->
                <c:if test="${not empty addresses}">
                    <c:forEach var="a" items="${addresses}">
                        <div class="address-item ${a.status == 1 ? 'default' : ''}" data-id="${a.id}">
                            <div class="address-row">
                                <span class="address-name">${a.name}</span> |
                                <span class="address-phone">${a.phoneNumber}</span>
                            </div>

                            <div class="address-details">${a.address}</div>

                            <div class="address-actions">
                                <c:if test="${a.status == 1}">
                                    <span class="address-default-badge">Mặc định</span>
                                </c:if>

                                <a href="javascript:void(0)" data-action="update" data-id="${a.id}">Sửa</a>
                                <a href="javascript:void(0)" data-action="delete" data-id="${a.id}">Xóa</a>

                                <c:if test="${a.status != 1}">
                                    <button data-action="set-default" data-id="${a.id}">
                                        Đặt mặc định
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>

                <!-- CHƯA CÓ ĐỊA CHỈ -->
                <c:if test="${empty addresses}">
                    <div class="address-empty">
                        <p>Bạn chưa có địa chỉ nào. Hãy thêm địa chỉ mới.</p>
                    </div>
                </c:if>

            </div>
        </div>
    </div>
</div>

<!-- MODAL ADD ADDRESS -->
<div class="modal-overlay" id="modalOverlay">
    <div class="modal-content">
        <h2 class="modal-title">Địa chỉ mới</h2>

        <form class="address-form" id="addressForm">
            <input type="hidden" id="addressId">

            <div class="form-row">
                <label class="form-label">Họ và tên</label>
                <input type="text" id="name" class="form-input" required>
            </div>

            <div class="form-row">
                <label class="form-label">Số điện thoại</label>
                <input type="text" id="phoneNumber" class="form-input" required>
            </div>

            <div class="form-row">
                <label class="form-label">Địa chỉ</label>
                <textarea id="fullAddress" class="form-textarea" rows="3" required></textarea>
            </div>

            <div class="form-row">
                <label class="checkbox-label">
                    <input type="checkbox" id="status">
                    <span>Đặt làm địa chỉ mặc định</span>
                </label>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn-back" id="btnBack">Trở lại</button>
                <button type="submit" class="btn-complete">Hoàn thành</button>
            </div>
        </form>
    </div>
</div>

<script>
    window.contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/header.js"></script>
<script src="${pageContext.request.contextPath}/js/addresses.js"></script>

</body>
</html>
