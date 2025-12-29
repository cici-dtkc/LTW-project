package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/listproduct")
public class ProductListServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Assuming category 1 is for phones
        List<Map<String, Object>> products = productService.getProductsByCategory(1);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/listproduct.jsp").forward(request, response);
    }
}