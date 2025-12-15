package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class ProductAdminServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        String category = req.getParameter("category");

        List<Product> products = productService.search(keyword, status, category);

        req.setAttribute("products", products);
        req.getRequestDispatcher("/views/admin/productAdmin.jsp")
                .forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productService.toggleStatus(id);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

}
