package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.hcmuaf.fit.webdynamic.model.*;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;
import vn.edu.hcmuaf.fit.webdynamic.util.FileUploadUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/product/add")
@MultipartConfig(
        fileSizeThreshold = 2 * 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 50 * 1024 * 1024
)
public class ProductAddController extends HttpServlet {
    private ProductService productService ;
    @Override
    public void init()   {
     productService = new ProductServiceImpl();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy thông tin cơ bản Product (Y hệt code cũ)
            String name = request.getParameter("productName");
            String description = request.getParameter("description");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));

            // Xử lý Brand (Giữ nguyên y hệt logic của bạn)
            String brandParam = request.getParameter("brandId");
            Brand brand;
            if ("custom".equals(brandParam)) {
                brand = new Brand();
                brand.setName(request.getParameter("customBrand"));
            } else {
                brand = new Brand(Integer.parseInt(brandParam));
            }

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setCategory(new Category(categoryId));
            product.setBrand(brand);
            product.setStatus(1);
            product.setCreatedAt(LocalDateTime.now());

            // 2. Xử lý Ảnh (Giữ nguyên logic của bạn)
            Part mainImagePart = request.getPart("productImage");
            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                String path = FileUploadUtil.saveImage(mainImagePart, getServletContext().getRealPath("/"));
                product.setMainImage(path);
            }

            // 3. Lấy các mảng tham số (Giữ nguyên tên biến của bạn)
            String[] techNames = request.getParameterValues("techName[]");
            String[] techValues = request.getParameterValues("techValue[]");
            String[] techPriorities = request.getParameterValues("techPriority[]");

            String[] variantNames = request.getParameterValues("variantName[]");
            String[] basePrices = request.getParameterValues("basePrice[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] variantQuantities = request.getParameterValues("variantQuantity[]");
            String[] skus = request.getParameterValues("sku[]");
            String[] colorVariantIndexes = request.getParameterValues("colorVariantIndex[]");
            String[] colorIds = request.getParameterValues("colorId[]");
            String[] customColors = request.getParameterValues("customColor[]");
            String[] colorPrices = request.getParameterValues("colorPrice[]");

            // 4. Gọi Service
            productService.addProduct(product, techNames, techValues, techPriorities,
                    variantNames, basePrices, quantities, variantQuantities,
                    skus, colorVariantIndexes, colorIds, customColors, colorPrices);

            response.sendRedirect(request.getContextPath() + "/admin/products?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            error(request, response, "Lỗi: " + e.getMessage());
        }
    }
    private void error(HttpServletRequest req,
                       HttpServletResponse resp,
                       String msg)
            throws ServletException, IOException {

        req.setAttribute("error", msg);
        req.getRequestDispatcher("/views/admin/addProductAdmin.jsp")
                .forward(req, resp);
    }


}
