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

        request.getRequestDispatcher("/views/admin/addProductAdmin.jsp")
                .forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("productName");
        String description = request.getParameter("description");

        // NOTE (FIX): categoryId lấy từ hidden input (luôn tồn tại)
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String brandParam = request.getParameter("brandId");
        Brand brand;

        if ("custom".equals(brandParam)) {
            String customBrand = request.getParameter("customBrand");

            if (customBrand == null || customBrand.isBlank()) {
                error(request, response, "Vui lòng nhập tên hãng mới");
                return;
            }

            brand = new Brand();
            brand.setName(customBrand);


        } else {
            int brandId = Integer.parseInt(brandParam);
            brand = new Brand(brandId);
        }


        Part mainImage = request.getPart("productImage");

        if (name == null || name.isBlank()) {
            error(request, response, "Tên sản phẩm không được để trống");
            return;
        }

        if (mainImage == null || mainImage.getSize() == 0) {
            error(request, response, "Chưa chọn ảnh đại diện");
            return;
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setStatus(1);
        product.setCreatedAt(LocalDateTime.now());
        product.setCategory(new Category(categoryId));
        product.setBrand(brand);

        try {
            String mainImagePath = FileUploadUtil.saveImage(
                    mainImage,
                    getServletContext().getRealPath("/")
            );
            product.setMainImage(mainImagePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*   TECH SPECS   */
        String[] techNames = request.getParameterValues("techName[]");
        String[] techValues = request.getParameterValues("techValue[]");
        String[] techPriorities = request.getParameterValues("techPriority[]");

        if (techNames != null) {
            List<TechSpecs> techList = new ArrayList<>();
            for (int i = 0; i < techNames.length; i++) {
                TechSpecs t = new TechSpecs();
                t.setName(techNames[i]);
                t.setValue(techValues[i]);
                t.setPriority(Integer.parseInt(techPriorities[i]));
                t.setCreatedAt(LocalDateTime.now()); // bổ sung createdAt
                techList.add(t);
            }
            product.setTechSpecs(techList);
        }


        /*    VARIANTS   */
        String[] variantNames = request.getParameterValues("variantName[]");
        String[] basePrices = request.getParameterValues("basePrice[]");

        if (variantNames == null || variantNames.length == 0) {
            error(request, response, "Phải có ít nhất 1 phiên bản");
            return;
        }

        List<ProductVariant> variants = new ArrayList<>();

        for (int i = 0; i < variantNames.length; i++) {
            ProductVariant v = new ProductVariant();
            v.setName(variantNames[i]);
            v.setBasePrice(Double.parseDouble(basePrices[i]));
            v.setStatus(1);
            v.setCreatedAt(LocalDateTime.now());
            variants.add(v);
        }

        /*   COLORS   */
        String[] colorIds = request.getParameterValues("colorId[]");
        String[] customColors = request.getParameterValues("customColor[]"); // NOTE
        String[] colorPrices = request.getParameterValues("colorPrice[]");
        String[] quantities = request.getParameterValues("quantity[]");

        List<Part> colorImageParts = request.getParts().stream()
                .filter(p -> "colorImage[]".equals(p.getName()) && p.getSize() > 0)
                .toList();

        int colorIndex = 0;

        for (ProductVariant variant : variants) {

            List<VariantColor> variantColors = new ArrayList<>();

            // NOTE: mỗi variant hiện xử lý 1 màu (ổn định, không crash)
            if (colorIds != null && colorIndex < colorIds.length) {

                VariantColor vc = new VariantColor();

                // NOTE : xử lý màu custom
                if ("custom".equals(colorIds[colorIndex])) {
                    Color c = new Color();
                    c.setName(customColors[colorIndex]);
                    vc.setColor(c);
                } else {
                    vc.setColor(new Color(Integer.parseInt(colorIds[colorIndex])));
                }

                vc.setPrice(Double.parseDouble(colorPrices[colorIndex]));
                vc.setQuantity(Integer.parseInt(quantities[colorIndex]));
                vc.setCreatedAt(LocalDateTime.now());

                // NOTE  : ảnh màu không bắt buộc
                if (colorIndex < colorImageParts.size()) {
                    try {
                        String imgPath = FileUploadUtil.saveImage(
                                colorImageParts.get(colorIndex),
                                getServletContext().getRealPath("/")
                        );

                        Image img = new Image();
                        img.setImgPath(imgPath);
                        img.setCreatedAt(LocalDateTime.now());

                        vc.setImages(List.of(img));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                variantColors.add(vc);
                colorIndex++;
            }

            variant.setColors(variantColors);
        }

        product.setVariants(variants);

        /*  SAVE   */
        productService.addPhone(product);

        response.sendRedirect(request.getContextPath() + "/admin/products");
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

