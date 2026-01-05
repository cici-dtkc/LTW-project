package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Order;
import vn.edu.hcmuaf.fit.webdynamic.service.OrderService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/orders")
public class OrderAdminServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    // ================== GET: LOAD + FILTER ==================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String statusFilterRaw = req.getParameter("statusFilter");
        String ajax = req.getParameter("ajax");

        Integer statusFilter = null;
        if (statusFilterRaw != null && !statusFilterRaw.isEmpty()) {
            statusFilter = Integer.parseInt(statusFilterRaw);
        }

        List<Map<String, Object>>  orders =
                (keyword != null && !keyword.isEmpty()) || statusFilter != null
                        ? orderService.searchForAdmin(keyword, statusFilter)
                        : orderService.getAllForAdmin();

        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/views/admin/orderAdmin.jsp")
                .forward(req, resp);
    }

    // ================== POST: UPDATE STATUS ==================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int newStatus = Integer.parseInt(req.getParameter("status"));

        Map<String, Object> result =
                orderService.updateStatus(orderId, newStatus);

        resp.setContentType("application/json;charset=UTF-8");

        boolean success = (boolean) result.get("success");
        String message = (String) result.get("message");

        resp.getWriter().print(String.format(
                "{\"success\": %s, \"message\": \"%s\"}",
                success,
                message.replace("\"", "\\\"")
        ));
    }
}



