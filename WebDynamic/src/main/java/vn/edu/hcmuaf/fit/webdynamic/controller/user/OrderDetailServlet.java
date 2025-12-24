package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdynamic.model.*;
import vn.edu.hcmuaf.fit.webdynamic.service.OrderDetailService;
import vn.edu.hcmuaf.fit.webdynamic.service.OrderService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "OrderDetailServlet", urlPatterns = { "/user/order-detail" })
public class OrderDetailServlet extends HttpServlet {

    private OrderService orderService;
    private OrderDetailService orderDetailService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        orderDetailService = new OrderDetailService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra user đã login chưa
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy orderId từ parameter
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/order");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);

            // Kiểm tra đơn hàng có thuộc về user không
            if (!orderService.isOrderBelongToUser(orderId, user.getId())) {
                response.sendRedirect(request.getContextPath() + "/user/order");
                return;
            }

            // Lấy thông tin đơn hàng
            Optional<Order> orderOpt = orderService.getOrderById(orderId);
            if (!orderOpt.isPresent()) {
                response.sendRedirect(request.getContextPath() + "/user/order");
                return;
            }

            Order order = orderOpt.get();

            // Lấy chi tiết đơn hàng
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsWithProduct(orderId);

            // Lấy thông tin địa chỉ giao hàng
            Address address = orderService.getOrderAddress(order.getAddressId());

            // Lấy thông tin phương thức thanh toán
            PaymentTypes paymentType = orderService.getPaymentType(order.getPaymentTypeId());

            // Đưa dữ liệu vào request
            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.setAttribute("address", address);
            request.setAttribute("paymentType", paymentType);
            request.setAttribute("orderService", orderService);
            request.setAttribute("orderDetailService", orderDetailService);

            // Forward đến JSP
            request.getRequestDispatcher("/views/user/order_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/order");
        }
    }
}