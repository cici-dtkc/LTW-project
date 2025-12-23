package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class OrderAdminServlet {
    @Override
    public void init() {
        productService = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Product> products = productService.getAllForAdmin();
        req.setAttribute("products", products);

        req.getRequestDispatcher("/views/admin/orderAdmin.jsp")
                .forward(req, resp);
    }
}


