package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.OrderService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/placeOrder")
public class CheckoutServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy dữ liệu từ form với validation
            String addressIdStr = request.getParameter("addressId");
            String paymentMethod = request.getParameter("payment");
            String voucherCode = request.getParameter("appliedVoucher");

            // Log để debug
            System.out.println("=== CHECKOUT DEBUG ===");
            System.out.println("User ID: " + user.getId());
            System.out.println("Address ID: " + addressIdStr);
            System.out.println("Payment: " + paymentMethod);
            System.out.println("Voucher: " + voucherCode);

            // Kiểm tra addressId
            if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                System.out.println("ERROR: addressId is null or empty");
                response.sendRedirect("cart?action=checkout&error=no_address");
                return;
            }

            int addressId;
            try {
                addressId = Integer.parseInt(addressIdStr);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid addressId format: " + addressIdStr);
                response.sendRedirect("cart?action=checkout&error=invalid_address");
                return;
            }

            // Kiểm tra payment method
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                System.out.println("ERROR: Payment method is null or empty");
                response.sendRedirect("cart?action=checkout&error=no_payment");
                return;
            }

            // Xử lý voucher (có thể null hoặc empty)
            if (voucherCode != null && voucherCode.trim().isEmpty()) {
                voucherCode = null;
            }

            // Lấy giỏ hàng
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                System.out.println("ERROR: Cart is null or empty");
                response.sendRedirect("cart?action=view");
                return;
            }

            System.out.println("Cart size: " + cart.size());
            int paymentStatus;
            if ("bank".equalsIgnoreCase(paymentMethod)) {
                paymentStatus = 3; // Đã thanh toán
                System.out.println("Payment status: Đã thanh toán (3)");
            } else {
                paymentStatus = 1; // Chưa thanh toán (COD)
                System.out.println("Payment status: Chưa thanh toán (1)");
            }
            // Gọi xử lý đặt hàng
            int orderId = orderService.processOrder(user.getId(), addressId, paymentMethod, voucherCode, cart, paymentStatus);

            System.out.println("Order created with ID: " + orderId);

            if (orderId > 0) {
                // Xóa giỏ hàng sau khi đặt thành công
                session.removeAttribute("cart");
                session.setAttribute("cartItemCount", 0);

                System.out.println("Order placed successfully, redirecting to order detail");
                // Chuyển hướng đến trang chi tiết đơn hàng
                response.sendRedirect(request.getContextPath() + "/user/order-detail?orderId=" + orderId);
            } else {
                System.out.println("ERROR: Order creation failed (orderId <= 0)");
                response.sendRedirect("cart?action=checkout&error=order_failed");
            }

        } catch (NumberFormatException e) {
            System.out.println("ERROR NumberFormatException: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("cart?action=checkout&error=invalid_number");
        } catch (Exception e) {
            System.out.println("ERROR Exception: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("cart?action=checkout&error=system_error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("cart?action=view");
    }
}