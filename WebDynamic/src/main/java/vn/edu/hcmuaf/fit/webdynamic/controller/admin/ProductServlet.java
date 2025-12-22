package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.model.ProductVariant;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");

        Integer status = parseInteger(req.getParameter("status"));
        Integer categoryId = parseInteger(req.getParameter("categoryId"));

        int page = parsePage(req.getParameter("page"));
        int limit = 10;
        int offset = (page - 1) * limit;

        /* ================== SERVICE ================== */
        List<Product> products =
                productService.getForAdmin(keyword, status, categoryId, page, limit);

        int total = productService.countForAdmin(keyword, status, categoryId);
        int totalPages = (int) Math.ceil((double) total / limit);

        /* ================== SET ATTRIBUTE ================== */
        req.setAttribute("products", products);
        req.setAttribute("totalVariants", total);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("page", page);

        // giữ filter khi render lại form
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);
        req.setAttribute("categoryId", categoryId);

        /* ================== FORWARD ================== */
        req.getRequestDispatcher("/views/admin/product.jsp")
                .forward(req, resp);
    }

    /* ================== UTILS ================== */

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parsePage(String value) {
        try {
            int p = Integer.parseInt(value);
            return p > 0 ? p : 1;
        } catch (Exception e) {
            return 1;
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("toggle".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            productService.toggleStatus(id);
        }

        // Redirect để load lại trạng thái mới
        resp.sendRedirect(req.getContextPath() + "/admin/products");}


}
