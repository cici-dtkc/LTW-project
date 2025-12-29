package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.IOException;
import java.util.Map;
@WebServlet("/admin/products/edit")
public class ProductEditServlet  extends HttpServlet {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int vcId = Integer.parseInt(req.getParameter("id"));

        Map<String, Object> product =
                productService.getProductForEditByVariantColorId(vcId);

        req.setAttribute("product", product);
        req.setAttribute("isEdit", true);

        req.getRequestDispatcher("/views/admin/addProductAdmin.jsp")
                .forward(req, resp);
    }
}
