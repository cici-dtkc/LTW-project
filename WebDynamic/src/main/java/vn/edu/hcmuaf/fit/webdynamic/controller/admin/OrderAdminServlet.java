package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.model.Order;
import vn.edu.hcmuaf.fit.webdynamic.service.OrderService;


import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class OrderAdminServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    // Hiển thị + search + filter
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String statusRaw = req.getParameter("status");

        Integer status = null;
        if (statusRaw != null && !statusRaw.isEmpty()) {
            status = Integer.parseInt(statusRaw);
        }

        List<Order> orders =
                (keyword != null || status != null)
                        ? orderService.searchForAdmin(keyword, status)
                        : orderService.getAllForAdmin();

        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
    }

    // Update trạng thái (AJAX)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int status = Integer.parseInt(req.getParameter("status"));

        boolean ok = orderService.updateOrderStatus(orderId, status);
        resp.getWriter().print(ok ? "success" : "fail");
    }

}


