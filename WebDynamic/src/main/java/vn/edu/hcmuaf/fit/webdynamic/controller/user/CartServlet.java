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
        try {
            String vcIdParam = request.getParameter("vcId");
            if (vcIdParam == null || vcIdParam.isEmpty() || "undefined".equals(vcIdParam)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            int vcId = Integer.parseInt(vcIdParam);
            cart.put(vcId, cart.getOrDefault(vcId, 0) + 1);

            syncCartSession(session, cart);

            response.setContentType("text/plain");
            response.getWriter().print(session.getAttribute("cartItemCount"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
            response.sendRedirect("login.jsp"); // Hoặc trang login của bạn
            return;
        }

        if (cart.isEmpty()) {
            response.sendRedirect("cart?action=view");
            return;
        }

        List<Map<String, Object>> displayList = getCartDetails(cart);
        double subtotal = displayList.stream().mapToDouble(i -> (double) i.get("subTotal")).sum();

        // 1. Load địa chỉ mặc định từ OrderService
        Address defaultAddress = orderService.getDefaultAddress(user.getId());

        // 2. Load danh sách Voucher từ VoucherAdminDao thông qua OrderService
        List<VoucherAdmin> availableVouchers = orderService.getActiveVouchers();
        System.out.println("DEBUG: User ID hiện tại = " + user.getId());
        System.out.println("DEBUG: Số lượng Voucher lấy được = " + (availableVouchers != null ? availableVouchers.size() : "NULL"));

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