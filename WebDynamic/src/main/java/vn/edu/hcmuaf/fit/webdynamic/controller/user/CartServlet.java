package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private ProductService productService = new ProductServiceImpl();

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

        // CartServlet.java
        if ("add".equals(action)) {
            try {
                String vcIdParam = request.getParameter("vcId");
                if (vcIdParam == null || vcIdParam.isEmpty() || "undefined".equals(vcIdParam)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                int vcId = Integer.parseInt(vcIdParam);

                // Thêm vào Map giỏ hàng
                cart.put(vcId, cart.getOrDefault(vcId, 0) + 1);

                // Lưu lại Map vào Session (Quan trọng)
                session.setAttribute("cart", cart);

                // Tính tổng số lượng hiển thị trên badge
                int totalQuantity = cart.values().stream().mapToInt(Integer::intValue).sum();
                session.setAttribute("cartItemCount", totalQuantity);

                // Phản hồi cho Ajax
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(totalQuantity);
                response.getWriter().flush();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        else if ("remove".equals(action)) {
            int vcId = Integer.parseInt(request.getParameter("vcId"));
            cart.remove(vcId); // Xóa khỏi bản đồ giỏ hàng

            // Cập nhật lại số lượng hiển thị trên icon giỏ hàng
            int totalQuantity = cart.values().stream().mapToInt(Integer::intValue).sum();
            session.setAttribute("cartItemCount", totalQuantity);

            response.sendRedirect("cart?action=view");
            return;
        }
        else if ("update".equals(action)) {
            int vcId = Integer.parseInt(request.getParameter("vcId"));
            int delta = Integer.parseInt(request.getParameter("delta"));

            int currentQty = cart.getOrDefault(vcId, 0);
            int newQty = currentQty + delta;

            if (newQty > 0) {
                cart.put(vcId, newQty);
            } else {
                cart.remove(vcId);
            }

            // Cập nhật lại số lượng hiển thị trên icon giỏ hàng
            int totalQuantity = cart.values().stream().mapToInt(Integer::intValue).sum();
            session.setAttribute("cartItemCount", totalQuantity);

            response.sendRedirect("cart?action=view");
            return;
        }

        else if ("view".equals(action)) {
            // Xử lý hiển thị trang giỏ hàng
            List<Map<String, Object>> displayList = new ArrayList<>();
            double totalCartPrice = 0;

            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                Map<String, Object> item = productService.getProductForCart(entry.getKey());
                if (item != null) {
                    int qty = entry.getValue();

                    BigDecimal priceBD = (BigDecimal) item.get("unit_price");
                    double priceFinal = priceBD.doubleValue();

                    double subTotal = priceFinal * qty;

                    item.put("quantity", qty);
                    item.put("subTotal", subTotal);
                    totalCartPrice += subTotal;
                    displayList.add(item);
                }
            }

            request.setAttribute("cartItems", displayList);
            request.setAttribute("totalCartPrice", totalCartPrice);
            request.getRequestDispatcher("/views/user/cart.jsp").forward(request, response);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}