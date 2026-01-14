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
            response.sendRedirect("/login");
            return;
        }

        try {
            // Lấy dữ liệu từ form
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            String paymentMethod = request.getParameter("payment");
            String voucherCode = request.getParameter("appliedVoucher"); // Lấy từ input hidden

            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                response.sendRedirect("cart?action=view");
                return;
            }

            // Gọi xử lý đặt hàng
            int orderId = orderService.processOrder(user.getId(), addressId, paymentMethod, voucherCode, cart);

            if (orderId > 0) {
                // Xóa giỏ hàng sau khi đặt thành công
                session.removeAttribute("cart");
                session.setAttribute("cartItemCount", 0);

                // Chuyển hướng đến trang thành công
                response.sendRedirect(request.getContextPath() + "/user/order-detail?id=" + orderId);
            } else {
                response.sendRedirect("cart?action=checkout&error=system");
            }
        } catch (Exception e) {
            System.out.println("LỖI TẠI CHECKOUT: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("cart?action=checkout&error=invalid_data");
        }

}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("cart?action=view");
    }
}