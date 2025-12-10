<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.User" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.Address" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) request.getAttribute("user");
    List<Address> addresses = (List<Address>) request.getAttribute("addresses");
    // Set activeMenu để highlight menu item trong sidebar
    request.setAttribute("activeMenu", "address");
%>
<html>
<head>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/addresses.css">
</head>
<body>
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
                <%
                    if (addresses != null && !addresses.isEmpty()) {
                        for (Address addr : addresses) {
                %>
                <!-- Địa chỉ 1 -->
                <div class="address-item <%= addr.getStatus()==1 ? "default" : ""%>" data-id="<%=addr.getId()%>">
                    <div class="address-info">
                        <div class="address-name-phone">
                            <span class="address-name"><%= addr.getName() != null ? addr.getName() : ""%></span>
                            <span class="address-separator">|</span>
                            <span class="address-phone"><%= addr.getPhoneNumber() != null ? addr.getPhoneNumber() : ""%></span>
                        </div>
                        <div class="address-details">
                            <div><%= addr.getFullAddress()!= null ? addr.getFullAddress() : ""%></div>
                        </div>
                        <% if (addr.getStatus() == 1) { %>
                        <span class="address-default-badge">Mặc định</span>
                        <% } %>
                    </div>

                    <div class="address-actions">
                        <div class="address-action-links">
                            <a href="#" class="address-action-link" data-action="update" data-id="<%= addr.getId() %>">Cập nhật</a>
                            <a href="#" class="address-action-link" data-action="delete" data-id="<%= addr.getId() %>">Xóa</a>
                        </div>
                        <button class="btn-set-default" data-action="set-default" data-id="<%= addr.getId() %>">
                            Thiết lập mặc định
                        </button>
                    </div>
                </div>
                <%
                      }
                    }else{
                        %>
                    <div class="address-empty">
                    <p>Bạn chưa có địa chỉ nào. Hãy thêm địa chỉ mới.</p>
                    </div>
                        <%
                    }
                %>
            </div>

        </div>
    </div>
</div>
<!-- Modal Add Address -->
<div class="modal-overlay" id="modalOverlay">
    <div class="modal-content">
        <h2 class="modal-title">Địa chỉ mới (dùng thông tin trước sáp nhập)</h2>

        <form class="address-form" id="addressForm">
            <div class="form-row">
                <label for="fullName" class="form-label">Họ và tên</label>
                <input type="text" id="fullName" class="form-input" placeholder="Nhập họ và tên" required>
            </div>

            <div class="form-row">
                <label for="phoneNumber" class="form-label">Số điện thoại</label>
                <input type="text" id="phoneNumber" class="form-input" placeholder="Nhập số điện thoại" required>
            </div>

            <div class="form-row">
                <label for="location" class="form-label">Tỉnh/ Thành phố, Quận/Huyện, Phường/Xã</label>
                <label for="specificAddress" class="form-label">Địa chỉ cụ thể</label>
                <textarea id="specificAddress" class="form-textarea" placeholder="Nhập địa chỉ cụ thể" rows="3" required></textarea>
            </div>
            <div class="form-row">
                <label class="checkbox-label">
                    <input type="checkbox" id="setDefault" class="checkbox-input">
                    <span>Đặt làm địa chỉ mặc định</span>
                </label>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn-back" id="btnBack">Trở Lại</button>
                <button type="submit" class="btn-complete">Hoàn thành</button>
            </div>
        </form>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/addresses.js"></script>
</html>
