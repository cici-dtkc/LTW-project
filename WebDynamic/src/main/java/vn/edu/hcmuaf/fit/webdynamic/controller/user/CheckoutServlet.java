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
import java.util.HashMap;
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

            String[] selectedIds = request.getParameterValues("selectedItems");
            Map<Integer, Integer> fullCart = (Map<Integer, Integer>) session.getAttribute("cart");

            if (selectedIds == null || fullCart == null) {
                response.sendRedirect("cart?action=view&error=no_items");
                return;
            }
            // giỏ hàng tạm thời chỉ gồm các mục đã chọn
            Map<Integer, Integer> orderCart = new HashMap<>();
            for (String idStr : selectedIds) {
                int vcId = Integer.parseInt(idStr);
                if (fullCart.containsKey(vcId)) {
                    orderCart.put(vcId, fullCart.get(vcId));
                }
            }
            // Gọi xử lý đặt hàng
            int orderId = orderService.processOrder(user.getId(), addressId, paymentMethod, voucherCode, orderCart);

            if (orderId > 0) {
                // Xóa giỏ hàng sau khi đặt thành công
                for (String idStr : selectedIds) {
                    fullCart.remove(Integer.parseInt(idStr));
                }
                session.setAttribute("cart", fullCart);
             int   newCount = fullCart.values().stream().mapToInt(Integer::intValue).sum();
                session.setAttribute("cartItemCount", newCount);
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