package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.Part;
import vn.edu.hcmuaf.fit.webdynamic.model.Category;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/products/edit")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ProductEditServlet  extends HttpServlet {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int vcId = Integer.parseInt(req.getParameter("id"));
        Map<String, Object> productMap = productService.getProductForEditByVariantColorId(vcId);

        // Gán vào biến 'product' cho form Phone
        req.setAttribute("product", productMap);

        // Kiểm tra category_id (1 là điện thoại)
        Object categoryId = productMap.get("category_id");
        boolean isPhone = categoryId != null && String.valueOf(categoryId).equals("1");
        req.setAttribute("isPhone", isPhone);

        // Nếu là linh kiện, gán thêm biến 'accessory' để JSP nhận diện đúng
        if (!isPhone) {
            req.setAttribute("accessory", productMap);
        }

        req.getRequestDispatcher("/views/admin/editProductAdmin.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            String name = req.getParameter("productName");
            String description = req.getParameter("description");

            String imagePath = req.getParameter("currentImage");

            Product p = new Product();
            p.setId(productId);
            p.setName(name);
            p.setDescription(description);
            p.setMainImage(imagePath);
            p.setCategory(new Category(categoryId));

            String[] techNames = req.getParameterValues("techNames[]");
            String[] techValues = req.getParameterValues("techValues[]");
            String[] techPriorities = req.getParameterValues("techPriorities[]");

            //   Lấy dữ liệu Variant/Color (Dùng toán tử điều kiện để gom dữ liệu)
            String[] vNames = req.getParameterValues("variantName");
            String[] bPrices = (categoryId == 1) ? req.getParameterValues("basePrice") : req.getParameterValues("basePrices[]");
            String[] vIds = (categoryId == 1) ? req.getParameterValues("variantId") : req.getParameterValues("variantIds[]");
            String[] cIds = (categoryId == 1) ? req.getParameterValues("colorId") : req.getParameterValues("colorIds[]");
            String[] qtys = (categoryId == 1) ? req.getParameterValues("quantity") : req.getParameterValues("variantQuantities[]");
            String[] skus = req.getParameterValues("sku");
            String[] cPrices = (categoryId == 1) ? req.getParameterValues("colorPrice") : req.getParameterValues("colorPrices[]");

            //   Gọi Service cập nhật
            productService.updateProduct(p, techNames, techValues, techPriorities,
                    vNames, bPrices, vIds, cIds, skus, qtys, cPrices);

            resp.sendRedirect(req.getContextPath() + "/admin/products?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/products?status=error");
        }
    }
}