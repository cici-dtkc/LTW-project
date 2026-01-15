<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.model.*" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.service.OrderService" %>
<%@ page import="vn.edu.hcmuaf.fit.webdynamic.service.OrderDetailService" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Lấy dữ liệu từ servlet
    Order order = (Order) request.getAttribute("order");
    List<OrderDetail> orderDetails = (List<OrderDetail>) request.getAttribute("orderDetails");
    Address address = (Address) request.getAttribute("address");
    PaymentTypes paymentType = (PaymentTypes) request.getAttribute("paymentType");
    OrderService orderService = (OrderService) request.getAttribute("orderService");
    OrderDetailService orderDetailService = (OrderDetailService) request.getAttribute("orderDetailService");

    // Format tiền tệ và ngày
    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Tính toán
    double subtotal = 0;
    for (OrderDetail detail : orderDetails) {
        subtotal += detail.getTotalMoney();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
    <meta charset="UTF-8">
    <title>Chi Tiết Đơn Hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/order_detail.css">
</head>

<body>
<jsp:include page="/views/includes/header.jsp"/>

<main class="order-detail-container">

    <section class="card" aria-labelledby="order-heading">
        <header class="order-header">
            <div>
                <div id="order-heading" class="order-id">Đơn hàng #OD<%= String.format("%06d", order.getId()) %></div>
                <div class="meta">Ngày đặt: <strong id="order-date"><%= dateFormat.format(order.getCreatedAt()) %></strong> • Người nhận: <span
                        id="order-name"><%= address != null ? address.getName() : "N/A" %></span></div>
            </div>
            <div style="text-align:right">
                <div id="paid-badge" class="status-pill" aria-live="polite">
                    <%
                        String paymentStatusText;
                        String paymentStatusClass;

                        // Kiểm tra phương thức thanh toán
                        // Nếu là chuyển khoản ngân hàng (ID = 2) thì đã thanh toán
                        // Nếu là thanh toán khi nhận hàng (ID = 1) thì chưa thanh toán
                        if (paymentType != null && paymentType.getId() == 2) {
                            paymentStatusText = "Đã thanh toán";
                            paymentStatusClass = "status-paid";
                        } else {
                            paymentStatusText = "Chưa thanh toán";
                            paymentStatusClass = "status-unpaid";
                        }
                    %>
                    <span id="paid-text" class="<%= paymentStatusClass %>"><%= paymentStatusText %></span>
                </div>
                <div class="muted" style="font-size:12px;margin-top:6px" id="order-status-small">
                    Trạng thái đơn hàng: <strong id="order-status"><%= OrderService.getStatusName(order.getStatus()) %></strong>
                </div>
            </div>
        </header>

        <div class="items" id="order-items" aria-live="polite">
            <% for (OrderDetail detail : orderDetails) {
                Map<String, String> productInfo = orderDetailService.getProductInfoByVariantId(detail.getVariantId());
            %>
            <article class="item">
                <div class="thumb" aria-hidden="true">
                    <img src="${pageContext.request.contextPath}/<%= productInfo.get("imagePath") != null ? productInfo.get("imagePath") : "assert/img/product/default.jpg" %>"
                         alt="<%= productInfo.get("productName") %>"
                         style="width:82px;height:82px;border-radius:8px;object-fit:cover"/>
                </div>
                <div class="item-info">
                    <div class="name"><%= productInfo.get("productName") %> <%= productInfo.get("variantName") %></div>
                    <div class="variant">Màu: <%= productInfo.get("colorName") %> • Bảo hành: 12 tháng</div>
                    <div class="small" style="margin-top:8px">Mã SP: <span class="muted">TWX<%= String.format("%04d", detail.getVariantId()) %>-<%= productInfo.get("colorCode") %></span></div>
                </div>
                <div class="price-qty">
                    <div class="price" id="item<%= detail.getId() %>-price"><%= currencyFormat.format(detail.getPrice()) %>₫</div>
                    <div class="qty">x<%= detail.getQuantity() %></div>
                </div>
            </article>
            <% } %>
        </div>

        <div class="totals" role="region" aria-label="Tổng tiền">
            <div class="row">
                <div class="small">Tạm tính</div>
                <div id="subtotal"><%= currencyFormat.format(subtotal) %>₫</div>
            </div>
            <div class="row">
                <div class="small">Phí vận chuyển</div>
                <div id="ship-fee"><%= currencyFormat.format(order.getFeeShipping()) %>₫</div>
            </div>
            <div class="row">
                <div class="small">Giảm giá</div>
                <div id="discount"><%= currencyFormat.format(order.getDiscountAmount()) %>₫</div>
            </div>
            <div class="row total-amount">
                <div>Tổng cộng</div>
                <div id="total"><%= currencyFormat.format(order.getTotalAmount()) %>₫</div>
            </div>

            <div class="actions" role="group" aria-label="Hành động đơn hàng">
                <% if (order.getStatus() == 1) { %>
                <button id="btn-request-cancel" class="btn-ghost" onclick="cancelOrder(<%= order.getId() %>)">Yêu cầu hủy</button>
                <% } %>
            </div>

            <footer class="note">Ghi chú: Bạn có thể yêu cầu hủy nếu đơn hàng chưa giao.</footer>

        </div>
    </section>

    <aside class="right">
        <div class="card section" aria-labelledby="summary-heading">
            <h3 id="summary-heading" style="margin:0 0 8px 0">Tóm tắt</h3>
            <div class="small">Phương thức thanh toán</div>
            <div style="display:flex;justify-content:space-between;align-items:center;margin-top:8px">
                <div>
                    <div id="payment-method"><%= paymentType != null ? paymentType.getName() : "N/A" %></div>
                </div>
            </div>
        </div>

        <div class="card section" aria-labelledby="address-heading">
            <h4 id="address-heading" style="margin:0 0 8px 0">Giao tới</h4>
            <div class="address">
                <div id="rec-name"><strong><%= address != null ? address.getAddress(): "N/A" %></strong> • <span class="muted"
                                                                         id="rec-phone"><%= address != null ? address.getPhoneNumber() : "N/A" %></span></div>
                <div id="ship-address" class="muted"><%= address != null ? address.getAddress() : "N/A" %></div>
                <div id="ship-phone" class="muted"><%= address != null ? address.getPhoneNumber() : "N/A" %></div>
            </div>
        </div>
    </aside>
</main>

<script src="${pageContext.request.contextPath}/js/orderDetail.js"></script>
<script src="${pageContext.request.contextPath}/js/header.js"></script>

<script>
function cancelOrder(orderId) {
    if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
        fetch('${pageContext.request.contextPath}/user/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'action=cancel&orderId=' + orderId
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Hủy đơn hàng thành công');
                location.reload();
            } else {
                alert('Lỗi: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi hủy đơn hàng');
        });
    }
}
</script>

</body>
</html>