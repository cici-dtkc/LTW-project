<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.Order" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.OrderDetail" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.service.OrderService" %>
<%@ page import="java.util.Map" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.service.OrderDetailService" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%
    // Lấy dữ liệu từ servlet
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    OrderService orderService = (OrderService) request.getAttribute("orderService");
    OrderDetailService orderDetailService = (OrderDetailService) request.getAttribute("orderDetailService");
    String currentStatus = (String) request.getAttribute("currentStatus");

    // Format tiền tệ
    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn Mua - Cửa hàng điện thoại</title>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/order.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/notification.css">
</head>

<body>
<jsp:include page="/views/includes/header.jsp"/>

<div id="pageWrapper">
    <!-- SIDEBAR -->
    <jsp:include page="/views/includes/sidebarUser.jsp"/>

    <!-- ================== MAIN CONTENT ================== -->
    <div class="orders-section">
        <h2>Đơn Mua Của Tôi</h2>

        <!-- Thanh trạng thái -->
        <div class="status-tabs">
            <button class="tab <%= (currentStatus == null || "all".equals(currentStatus)) ? "active" : "" %>"
                    data-status="all"
                    onclick="location.href='<%= request.getContextPath() %>/user/order?status=all'">Tất cả</button>
            <button class="tab <%= "1".equals(currentStatus) ? "active" : "" %>"
                    data-status="prepare"
                    onclick="location.href='<%= request.getContextPath() %>/user/order?status=1'">Đang lên đơn</button>
            <button class="tab <%= "2".equals(currentStatus) ? "active" : "" %>"
                    data-status="shipping"
                    onclick="location.href='<%= request.getContextPath() %>/user/order?status=2'">Đang giao</button>
            <button class="tab <%= "3".equals(currentStatus) ? "active" : "" %>"
                    data-status="delivered"
                    onclick="location.href='<%= request.getContextPath() %>/user/order?status=3'">Đã giao</button>
            <button class="tab <%= "5".equals(currentStatus) ? "active" : "" %>"
                    data-status="cancelled"
                    onclick="location.href='<%= request.getContextPath() %>/user/order?status=4'">Đã hủy</button>
        </div>

        <!-- Danh sách đơn -->
        <div class="orders-list">
            <%
                if (orders == null || orders.isEmpty()) {
            %>
            <div style="text-align: center; padding: 50px; color: #666;">
                <i class="fa-solid fa-box-open" style="font-size: 60px; color: #ddd;"></i>
                <p style="margin-top: 20px; font-size: 16px;">Chưa có đơn hàng nào</p>
            </div>
            <%
            } else {
                for (Order order : orders) {
                    List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsWithProduct(order.getId());
                    String statusClass = OrderService.getStatusClass(order.getStatus());
                    String statusName = OrderService.getStatusName(order.getStatus());
                    String statusIcon = OrderService.getStatusIcon(order.getStatus());
            %>

            <!-- Đơn hàng -->
            <div class="order-card" data-status="<%= statusClass %>" data-order-id="<%= order.getId() %>">
                <%
                    if (orderDetails.size() > 1) {
                %>
                <div class="order-items">
                    <% for (OrderDetail detail : orderDetails) { 
                        Map<String, String> productInfo = orderDetailService.getProductInfoByVariantId(detail.getVariantId());
                    %>
                    <a href="<%= request.getContextPath() %>/user/order-detail?orderId=<%= order.getId() %>">
                        <div class="order-info">
                            <img src="<%= request.getContextPath() %>/<%= productInfo.get("imagePath") != null ? productInfo.get("imagePath") : "assert/img/product/default.jpg" %>"
                                 alt="<%= productInfo.get("productName") %>">
                            <div class="order-detail">
                                <h3><%= productInfo.get("productName") %> <%= productInfo.get("variantName") %></h3>
                                <p>Số lượng: <%= detail.getQuantity() %></p>
                                <p>Giá: <span class="price"><%= currencyFormat.format(detail.getPrice()) %>đ</span></p>
                            </div>
                        </div>
                    </a>
                    <% } %>
                </div>
                <%
                } else if (!orderDetails.isEmpty()) {
                    OrderDetail detail = orderDetails.get(0);
                    Map<String, String> productInfo = orderDetailService.getProductInfoByVariantId(detail.getVariantId());
                %>
                <a href="<%= request.getContextPath() %>/user/order-detail?orderId=<%= order.getId() %>">
                    <div class="order-info">
                        <img src="<%= request.getContextPath() %>/<%= productInfo.get("imagePath") != null ? productInfo.get("imagePath") : "assert/img/product/default.jpg" %>"
                             alt="<%= productInfo.get("productName") %>">
                        <div class="order-detail">
                            <h3><%= productInfo.get("productName") %> <%= productInfo.get("variantName") %></h3>
                            <p>Số lượng: <%= detail.getQuantity() %></p>
                            <p>Giá: <span class="price"><%= currencyFormat.format(detail.getPrice()) %>đ</span></p>
                        </div>
                    </div>
                </a>
                <% } %>

                <div class="order-footer">
                    <p class="total">Tổng cộng:
                        <span class="total-price"><%= currencyFormat.format(order.getTotalAmount()) %>đ</span>
                    </p>
                    <div class="order-status">
            <span class="status <%= statusClass %>">
              <i class="<%= statusIcon %>"></i> <%= statusName %>
            </span>
                        <div class="actions">
                            <% if (order.getStatus() == 1) { %>
                            <button class="btn repurchase" onclick="cancelOrder(<%= order.getId() %>)">Hủy</button>
                            <% } else if (order.getStatus() == 3 || order.getStatus() == 5) { %>
                            <button class="btn repurchase" onclick="repurchaseOrder(<%= order.getId() %>)">Mua lại</button>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>

            <%
                    } // end for
                } // end else
            %>
        </div>
    </div>
</div>

<!-- Toast Container -->
<div id="toast-container" style="position: fixed; top: 20px; right: 20px; z-index: 9999;"></div>
<script src="${pageContext.request.contextPath}/js/notification.js"></script>
<script src="${pageContext.request.contextPath}/js/order.js"></script>

<script src="${pageContext.request.contextPath}/js/header.js"></script>

</body>
</html>