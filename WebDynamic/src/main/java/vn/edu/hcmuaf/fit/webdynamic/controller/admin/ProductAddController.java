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

        // 1. Lấy thông tin cơ bản Product
        String name = request.getParameter("productName");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        // Xử lý Brand
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

        // 2. Xử lý Ảnh đại diện sản phẩm
        Part mainImagePart = request.getPart("productImage");
        if (mainImagePart != null && mainImagePart.getSize() > 0) {
            try {
                String path = FileUploadUtil.saveImage(mainImagePart, getServletContext().getRealPath("/"));
                product.setMainImage(path);
            } catch (Exception e) { e.printStackTrace(); }
        }

        // 3. Xử lý Thông số kỹ thuật
        String[] techNames = request.getParameterValues("techName[]");
        String[] techValues = request.getParameterValues("techValue[]");
        String[] techPriorities = request.getParameterValues("techPriority[]");

        if (techNames != null) {
            List<TechSpecs> techList = new ArrayList<>();
            for (int i = 0; i < techNames.length; i++) {
                if (techNames[i] == null || techNames[i].isBlank()) continue;
                TechSpecs t = new TechSpecs();
                t.setName(techNames[i]);
                t.setValue(techValues[i]);
                t.setPriority(Integer.parseInt(techPriorities[i]));
                techList.add(t);
            }
            product.setTechSpecs(techList);
        }

        /* 4. XỬ LÝ VARIANTS & COLORS */
        String[] variantNames = request.getParameterValues("variantName[]");
        String[] basePrices = request.getParameterValues("basePrice[]");
        String[] quantities = request.getParameterValues("quantity[]"); // Dùng cho Điện thoại (mảng màu)
        String[] variantQuantities = request.getParameterValues("variantQuantity[]"); // Dùng cho Linh kiện

        if (variantNames != null) {
            List<ProductVariant> variants = new ArrayList<>();

            // Dữ liệu màu (chỉ dùng cho điện thoại)
            String[] colorVariantIndexes = request.getParameterValues("colorVariantIndex[]");
            String[] colorIds = request.getParameterValues("colorId[]");
            String[] customColors = request.getParameterValues("customColor[]");
            String[] colorPrices = request.getParameterValues("colorPrice[]");

            for (int i = 0; i < variantNames.length; i++) {
                ProductVariant v = new ProductVariant();
                v.setName(variantNames[i]);
                v.setBasePrice(parseDbl(basePrices != null ? basePrices[i] : "0"));
                v.setStatus(1);
                v.setCreatedAt(LocalDateTime.now());

                List<VariantColor> variantColors = new ArrayList<>();

                if (categoryId == 1) {
                    // --- TRƯỜNG HỢP ĐIỆN THOẠI ---
                    if (colorIds != null && colorVariantIndexes != null) {
                        for (int j = 0; j < colorIds.length; j++) {
                            // Dùng parseInt an toàn để tránh lỗi chuỗi rỗng hoặc null
                            int belongsToVariant = parseInt(colorVariantIndexes[j]);

                            if (belongsToVariant == i) {
                                VariantColor vc = new VariantColor();
                                if ("custom".equals(colorIds[j])) {
                                    Color c = new Color();
                                    c.setName(customColors != null ? customColors[j] : "Màu mới");
                                    vc.setColor(c);
                                } else {
                                    vc.setColor(new Color(parseInt(colorIds[j])));
                                }
                                vc.setPrice(parseDbl(colorPrices != null ? colorPrices[j] : "0"));
                                vc.setQuantity(parseInt(quantities != null ? quantities[j] : "0"));
                                vc.setCreatedAt(LocalDateTime.now());
                                variantColors.add(vc);
                            }
                        }
                    }
                } else {
                    // --- TRƯỜNG HỢP LINH KIỆN ---
                    VariantColor vc = new VariantColor();
                    vc.setColor(new Color(1)); // Màu mặc định
                    vc.setPrice(0.0);
                    vc.setQuantity(parseInt(variantQuantities != null ? variantQuantities[i] : "0"));
                    vc.setCreatedAt(LocalDateTime.now());
                    variantColors.add(vc);
                }

                v.setColors(variantColors);
                variants.add(v);
            }
            product.setVariants(variants);
        }

        // 5. Lưu xuống Database
        productService.addPhone(product);

        // 6. Redirect về trang danh sách kèm thông báo thành công
        response.sendRedirect(request.getContextPath() + "/admin/products?status=success");
    }
    private void error(HttpServletRequest req,
                       HttpServletResponse resp,
                       String msg)
            throws ServletException, IOException {

        req.setAttribute("error", msg);
        req.getRequestDispatcher("/views/admin/addProductAdmin.jsp")
                .forward(req, resp);
    }

private int parseInt(String s) {
    if (s == null || s.trim().isEmpty()) return 0;
    return Integer.parseInt(s.trim());
}

private double parseDbl(String s) {
    if (s == null || s.trim().isEmpty()) return 0.0;
    return Double.parseDouble(s.trim());
}
}
