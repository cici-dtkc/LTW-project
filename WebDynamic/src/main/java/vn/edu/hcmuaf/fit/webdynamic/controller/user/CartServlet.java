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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/user/cart")
public class CartServlet extends HttpServlet {
    private ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Kiểm tra và ép kiểu an toàn
        Object obj = session.getAttribute("cart");
        Map<Integer, Integer> cartSession = null;

        if (obj == null) {
            cartSession = new LinkedHashMap<>();
            session.setAttribute("cart", cartSession);
        } else {
            cartSession = (Map<Integer, Integer>) obj;
        }

        String action = request.getParameter("action");
        if (action == null) action = "view";

        // Sử dụng try-catch để tránh lỗi NumberFormatException khi tham số truyền vào sai
        try {
            switch (action) {
                case "add":
                    int addId = Integer.parseInt(request.getParameter("vcId"));
                    cartSession.put(addId, cartSession.getOrDefault(addId, 0) + 1);
                    response.sendRedirect("cart?action=view");
                    break;

                case "update":
                    int updateId = Integer.parseInt(request.getParameter("vcId"));
                    int delta = Integer.parseInt(request.getParameter("delta"));
                    int currentQty = cartSession.getOrDefault(updateId, 0);
                    int newQty = currentQty + delta;

                    if (newQty <= 0) {
                        cartSession.remove(updateId);
                    } else {
                        cartSession.put(updateId, newQty);
                    }
                    response.sendRedirect("cart?action=view");
                    break;

                case "remove":
                    int removeId = Integer.parseInt(request.getParameter("vcId"));
                    cartSession.remove(removeId);
                    response.sendRedirect("cart?action=view");
                    break;

                case "view":
                    List<Map<String, Object>> displayList = new ArrayList<>();
                    double totalCartPrice = 0;

                    for (Map.Entry<Integer, Integer> entry : cartSession.entrySet()) {
                        // Service sẽ dựa vào vcId để lấy thông tin
                        Map<String, Object> item = productService.getProductForCart(entry.getKey());
                        if (item != null) {
                            int qty = entry.getValue();
                            //   Ép kiểu double cần cẩn thận với giá trị từ DB
                            double priceFinal = Double.parseDouble(item.get("price_final").toString());
                            double subTotal = priceFinal * qty;

                            item.put("quantity", qty);
                            item.put("subTotal", subTotal);
                            item.put("vc_id", entry.getKey()); // Truyền lại ID để xử lý ở nút xóa/sửa trên JSP

                            totalCartPrice += subTotal;
                            displayList.add(item);
                        }
                    }

                    request.setAttribute("cartItems", displayList);
                    request.setAttribute("totalCartPrice", totalCartPrice);
                    request.getRequestDispatcher("/views/user/cart.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("cart?action=view");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}