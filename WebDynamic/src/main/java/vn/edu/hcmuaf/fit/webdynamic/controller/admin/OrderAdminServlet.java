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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String statusRaw = req.getParameter("status");
        String ajax = req.getParameter("ajax");

        Integer status = null;
        if (statusRaw != null && !statusRaw.isEmpty()) {
            status = Integer.parseInt(statusRaw);
        }

        List<Order> orders =
                (keyword != null && !keyword.isEmpty()) || status != null
                        ? orderService.searchForAdmin(keyword, status)
                        : orderService.getAllForAdmin();

        req.setAttribute("orders", orders);

        // AJAX hoặc load trang đều dùng chung JSP
        req.getRequestDispatcher("/views/admin/orderAdmin.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int newStatus = Integer.parseInt(req.getParameter("status"));

        Map<String, Object> result = orderService.updateStatus(orderId, newStatus);

        resp.setContentType("application/json;charset=UTF-8");

        boolean success = (boolean) result.get("success");
        String message = (String) result.get("message");

        String json = String.format(
                "{\"success\": %s, \"message\": \"%s\"}",
                success,
                message.replace("\"", "\\\"")
        );

        resp.getWriter().print(json);
    }

}
