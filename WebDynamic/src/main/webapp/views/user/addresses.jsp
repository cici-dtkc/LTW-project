<%--
  Created by IntelliJ IDEA.
  User: mtri2
  Date: 12/6/2025
  Time: 9:07 PM
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

                <!-- Địa chỉ 1 -->
                <div class="address-item" data-id="1">
                    <div class="address-info">
                        <div class="address-name-phone">
                            <span class="address-name">Nguyễn Văn A</span>
                            <span class="address-separator">|</span>
                            <span class="address-phone">1234567</span>
                        </div>
                        <div class="address-details">
                            <div>nhà Số 13 khu phố tây B đông hoà dĩ an bình dương</div>
                            <div>Phường Đông Hòa, Thành Phố Dĩ An, Bình Dương</div>
                        </div>
                        <span class="address-default-badge">Mặc định</span>
                    </div>

                    <div class="address-actions">
                        <div class="address-action-links">
                            <a href="#" class="address-action-link" data-action="update" data-id="1">Cập nhật</a>
                        </div>
                        <button class="btn-set-default" data-action="set-default" data-id="1">
                            Thiết lập mặc định
                        </button>
                    </div>
                </div>

                <!-- Địa chỉ 2 -->
                <div class="address-item" data-id="2">
                    <div class="address-info">
                        <div class="address-name-phone">
                            <span class="address-name">Phạm Thị B</span>
                            <span class="address-separator">|</span>
                            <span class="address-phone">(+84) 852 399 329</span>
                        </div>
                        <div class="address-details">
                            <div>Ấp Đất Mới</div>
                            <div>Xã Long Phước, Huyện Long Thành, Đồng Nai</div>
                        </div>
                    </div>

                    <div class="address-actions">
                        <div class="address-action-links">
                            <a href="#" class="address-action-link" data-action="update" data-id="2">Cập nhật</a>
                            <a href="#" class="address-action-link" data-action="delete" data-id="2">Xóa</a>
                        </div>
                        <button class="btn-set-default" data-action="set-default" data-id="2">
                            Thiết lập mặc định
                        </button>
                    </div>
                </div>

                <!-- Địa chỉ 3 -->
                <div class="address-item" data-id="3">
                    <div class="address-info">
                        <div class="address-name-phone">
                            <span class="address-name">Nguyễn Văn C</span>
                            <span class="address-separator">|</span>
                            <span class="address-phone">(+84) 325 883 448</span>
                        </div>
                        <div class="address-details">
                            <div>300/23/16 nguyễn văn linh q7</div>
                            <div>Phường Bình Thuận, Quận 7, TP. Hồ Chí Minh</div>
                        </div>
                    </div>

                    <div class="address-actions">
                        <div class="address-action-links">
                            <a href="#" class="address-action-link" data-action="update" data-id="3">Cập nhật</a>
                            <a href="#" class="address-action-link" data-action="delete" data-id="3">Xóa</a>
                        </div>
                        <button class="btn-set-default" data-action="set-default" data-id="3">
                            Thiết lập mặc định
                        </button>
                    </div>
                </div>

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
                <select id="location" class="form-select" required>
                    <option value="">Chọn địa điểm</option>
                    <option value="Phường Đông Hòa, Thành Phố Dĩ An, Bình Dương">Phường Đông Hòa, Thành Phố Dĩ An, Bình Dương</option>
                    <option value="Xã Hòa Hiệp, Huyện Cư Kuin, Đắk Lắk">Xã Hòa Hiệp, Huyện Cư Kuin, Đắk Lắk</option>
                    <option value="Xã Long Phước, Huyện Long Thành, Đồng Nai">Xã Long Phước, Huyện Long Thành, Đồng Nai</option>
                    <option value="Phường Bình Thuận, Quận 7, TP. Hồ Chí Minh">Phường Bình Thuận, Quận 7, TP. Hồ Chí Minh</option>
                    <option value="Phường 5, Quận Tân Bình, TP. Hồ Chí Minh">Phường 5, Quận Tân Bình, TP. Hồ Chí Minh</option>
                </select>
            </div>

            <div class="form-row">
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
<script src="${pageContext.request.contextPath}/js/addresses.js"></script>
</body>
</html>
