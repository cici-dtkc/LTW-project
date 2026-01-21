package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.edu.hcmuaf.fit.webdynamic.model.Category;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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

            Product p = new Product();
            p.setId(productId);
            p.setName(req.getParameter("productName"));
            p.setDescription(req.getParameter("description"));
            p.setMainImage(req.getParameter("currentImage"));
            p.setCategory(new Category(categoryId));

            System.out.println("===== SERVLET DEBUG =====");
            System.out.println("productId = " + productId);
            System.out.println("categoryId = " + categoryId);
            System.out.println("productName = " + p.getName());
            System.out.println("description = " + p.getDescription());
            System.out.println("image = " + p.getMainImage());

            String[] techNames = req.getParameterValues("techNames[]");
            String[] techValues = req.getParameterValues("techValues[]");
            String[] techPriorities = req.getParameterValues("techPriorities[]");
            System.out.println("techNames = " + Arrays.toString(techNames));
            System.out.println("techValues = " + Arrays.toString(techValues));
            System.out.println("techPriorities = " + Arrays.toString(techPriorities));
            // ================= PHONE =================
            if (categoryId == 1) {
                String vName      = req.getParameter("variantName");
                String basePrice  = req.getParameter("basePrice");
                String variantId  = req.getParameter("variantId");
                String colorId    = req.getParameter("colorId");
                String quantity   = req.getParameter("quantity");
                String sku        = req.getParameter("sku");
                String colorPrice = req.getParameter("colorPrice");

                System.out.println("=== PHONE DEBUG ===");
                System.out.println("variantName = " + vName);
                System.out.println("basePrice = " + basePrice);
                System.out.println("variantId = " + variantId);
                System.out.println("colorId = " + colorId);
                System.out.println("quantity = " + quantity);
                System.out.println("sku = [" + sku + "]");
                System.out.println("colorPrice = " + colorPrice);


                productService.updateProduct(
                        p,
                        techNames, techValues, techPriorities,
                        new String[]{vName},
                        new String[]{basePrice},
                        new String[]{variantId},
                        new String[]{colorId},
                        new String[]{sku},
                        new String[]{quantity},
                        new String[]{colorPrice}
                );
            }
            // ================= ACCESSORY =================
            else {
                String[] vNames   = req.getParameterValues("variantNames[]");
                String[] vIds     = req.getParameterValues("variantIds[]");
                String[] cIds     = req.getParameterValues("colorIds[]");
                String[] qtys     = req.getParameterValues("variantQuantities[]");
                String[] cPrices  = req.getParameterValues("colorPrices[]");

                System.out.println("=== ACCESSORY DEBUG ===");
                System.out.println("variantNames = " + Arrays.toString(vNames));
                System.out.println("variantIds = " + Arrays.toString(vIds));
                System.out.println("colorIds = " + Arrays.toString(cIds));
                System.out.println("quantities = " + Arrays.toString(qtys));
                System.out.println("colorPrices = " + Arrays.toString(cPrices));


                for (int i = 0; i < vIds.length; i++) {
                    System.out.println(
                            "ACCESSORY[" + i + "] vId=" + vIds[i] +
                                    ", vcId=" + cIds[i] +
                                    ", qty=" + qtys[i] +
                                    ", price=" + cPrices[i]
                    );
                }


                productService.updateProduct(
                        p,
                        techNames, techValues, techPriorities,
                        vNames,
                        cPrices,     // base price = color price
                        vIds,
                        cIds,
                        null,
                        qtys,
                        cPrices
                );
            }

            HttpSession session = req.getSession();
            session.setAttribute("toastMessage", "Đã chỉnh sửa");
            session.setAttribute("toastType", "success");

            resp.sendRedirect(req.getContextPath() + "/admin/products?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/products?status=error");
        }
    }

}