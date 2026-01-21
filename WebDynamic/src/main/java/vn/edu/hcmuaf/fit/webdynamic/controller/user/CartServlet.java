package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;
import vn.edu.hcmuaf.fit.webdynamic.service.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if (action == null) action = "view";

        switch (action) {
            case "add":
                addToCart(request, response, cart, session);
                break;
            case "remove":
                removeFromCart(request, response, cart, session);
                break;
            case "update":
                updateQuantity(request, response, cart, session);
                break;
            case "view":
                showCartPage(request, response, cart);
                break;
            case "checkout":
                prepareCheckoutPage(request, response, cart, session);
                break;
            default:
                response.sendRedirect("home");
                break;
        }
    }

    // --- CÁC PHƯƠNG THỨC XỬ LÝ RIÊNG BIỆT ---

    private void addToCart(HttpServletRequest request, HttpServletResponse response, Map<Integer, Integer> cart, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");

        // NẾU CHƯA ĐĂNG NHẬP
        if (user == null) {
            response.reset(); // Xóa bỏ mọi thứ định gửi đi
            response.setStatus(401); // Gửi mã 401 để JS biết là chưa login
            response.setContentType("text/plain");
            response.getWriter().print("LOGIN_REQUIRED");
            return;
        }

        // NẾU ĐÃ ĐĂNG NHẬP THÌ MỚI CHẠY TIẾP
        try {
            String vcIdParam = request.getParameter("vcId");
            int vcId = Integer.parseInt(vcIdParam);
            cart.put(vcId, cart.getOrDefault(vcId, 0) + 1);
            syncCartSession(session, cart);

            response.setContentType("text/plain");
            response.getWriter().print(session.getAttribute("cartItemCount"));
        } catch (Exception e) {
            response.setStatus(400);
        }
    }


    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, Map<Integer, Integer> cart, HttpSession session) throws IOException {
        int vcId = Integer.parseInt(request.getParameter("vcId"));
        cart.remove(vcId);
        syncCartSession(session, cart);
        response.sendRedirect("cart?action=view");
    }

    private void updateQuantity(HttpServletRequest request, HttpServletResponse response, Map<Integer, Integer> cart, HttpSession session) throws IOException {
        int vcId = Integer.parseInt(request.getParameter("vcId"));
        int delta = Integer.parseInt(request.getParameter("delta"));
        int newQty = cart.getOrDefault(vcId, 0) + delta;

        if (newQty > 0) cart.put(vcId, newQty);
        else cart.remove(vcId);

        syncCartSession(session, cart);
        response.sendRedirect("cart?action=view");
    }

    private void showCartPage(HttpServletRequest request, HttpServletResponse response, Map<Integer, Integer> cart) throws ServletException, IOException {
        List<Map<String, Object>> displayList = getCartDetails(cart);
        double total = displayList.stream().mapToDouble(i -> (double) i.get("subTotal")).sum();

        request.setAttribute("cartItems", displayList);
        request.setAttribute("totalCartPrice", total);
        request.getRequestDispatcher("/views/user/cart.jsp").forward(request, response);
    }

    /**
     * NHÁNH CHECKOUT: Load địa chỉ, voucher và forward sang checkout.jsp
     */
    private void prepareCheckoutPage(HttpServletRequest request, HttpServletResponse response, Map<Integer, Integer> cart, HttpSession session) throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }
        // Kiểm tra giỏ hàng tổng
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("cart?action=view&error=empty_cart");
            return;
        }
        //  Lấy danh sách ID được chọn từ URL
        String selectedIdsParam = request.getParameter("selectedIds");
        if (selectedIdsParam == null || selectedIdsParam.isEmpty()) {
            response.sendRedirect("cart?action=view&error=no_items");
            return;
        }

        // 3. Lọc sản phẩm an toàn
        Map<Integer, Integer> checkoutCart = new LinkedHashMap<>();
        String[] ids = selectedIdsParam.split(",");
        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                if (cart.containsKey(id)) {
                    checkoutCart.put(id, cart.get(id));
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi định dạng ID: " + idStr);
            }
        }
        if (checkoutCart.isEmpty()) {
            response.sendRedirect("cart?action=view&error=invalid_selection");
            return;
        }
try{
    List<Map<String, Object>> displayList = getCartDetails(checkoutCart);
        double subtotal = displayList.stream().mapToDouble(i -> (double) i.get("subTotal")).sum();

        // 1. Load địa chỉ mặc định từ OrderService
        Address defaultAddress = orderService.getDefaultAddress(user.getId());

        // 2. Load danh sách Voucher từ VoucherAdminDao thông qua OrderService
        List<VoucherAdmin> availableVouchers = orderService.getActiveVouchers();
        request.setAttribute("availableVouchers", availableVouchers);
        // 3. Set Attributes cho JSP
        request.setAttribute("cartItems", displayList);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("defaultAddress", defaultAddress);
        request.setAttribute("availableVouchers", availableVouchers);
        request.setAttribute("shippingFee", 30000.0);
        request.setAttribute("discountAmount", 0.0);

        request.getRequestDispatcher("/views/user/checkout.jsp").forward(request, response);
    }
catch (Exception e) {
        System.out.println("LỖI TẠI PREPARE CHECKOUT: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect("cart?action=view&error=system_error");
    }}
    // --- HÀM HỖ TRỢ (HELPER METHODS) ---

    private List<Map<String, Object>> getCartDetails(Map<Integer, Integer> cart) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Map<String, Object> item = productService.getProductForCart(entry.getKey());
            if (item != null) {
                int qty = entry.getValue();
                double price = ((BigDecimal) item.get("unit_price")).doubleValue();
                item.put("quantity", qty);
                item.put("subTotal", price * qty);
                item.put("vc_id", entry.getKey());
                list.add(item);
            }
        }
        return list;
    }

    private void syncCartSession(HttpSession session, Map<Integer, Integer> cart) {
        int totalQuantity = cart.values().stream().mapToInt(Integer::intValue).sum();
        session.setAttribute("cart", cart);
        session.setAttribute("cartItemCount", totalQuantity);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}