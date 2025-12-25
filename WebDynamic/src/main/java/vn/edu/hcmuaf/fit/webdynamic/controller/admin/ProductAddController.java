package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
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


public class ProductAddController extends HttpServlet {
    private ProductService productService ;
    @Override
    public void init()   {
     productService = new ProductServiceImpl();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("productName");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int brandId = Integer.parseInt(request.getParameter("brandId"));
        Part mainImage =request.getPart("productImage");
        if (name == null || name.isBlank()) {
            error(request, response, "Tên sản phẩm không được để trống");
            return;
        }

        if (mainImage == null || mainImage.getSize() == 0) {
            error(request, response , "Chưa chọn ảnh đại diện");
            return;
        }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setStatus(1);
        product.setCreatedAt(LocalDateTime.now());

        product.setCategory(new Category(categoryId));
        product.setBrand(new Brand(brandId));
        String mainImagePath = null;
        try {
            mainImagePath = FileUploadUtil.saveImage(mainImage, getServletContext().getRealPath("/"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        product.setMainImage(mainImagePath);

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
        String[] colorPrices = request.getParameterValues("colorPrice[]");
        String[] quantities = request.getParameterValues("quantity[]");

        List<Part> colorImageParts = request.getParts().stream()
                .filter(p -> "colorImage[]".equals(p.getName()) && p.getSize() > 0)
                .toList();

        int colorIndex = 0;

        for (ProductVariant variant : variants) {

            int colorCount = Integer.parseInt(
                    request.getParameter("colorCount_" + variant.getName())
            );

            List<VariantColor> variantColors = new ArrayList<>();

            for (int j = 0; j < colorCount; j++) {

                VariantColor vc = new VariantColor();

                //   COLOR
                int colorId = Integer.parseInt(colorIds[colorIndex]);
                vc.setColor(new Color(colorId));

                //   PRICE & QUANTITY
                vc.setPrice(Double.parseDouble(colorPrices[colorIndex]));
                vc.setQuantity(Integer.parseInt(quantities[colorIndex]));
                vc.setCreatedAt(LocalDateTime.now());

                //   IMAGE
                Part imagePart = colorImageParts.get(colorIndex);
                String imagePath = null;
                try {
                    imagePath = FileUploadUtil.saveImage(
                            imagePart,
                            getServletContext().getRealPath("/")
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Image img = new Image();
                img.setImgPath(imagePath);
                img.setCreatedAt(LocalDateTime.now());

                vc.setImages(List.of(img));

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
        req.getRequestDispatcher("/admin/addProduct.jsp")
                .forward(req, resp);
    }
    }

